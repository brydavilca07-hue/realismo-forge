package com.realismo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod(RealismoMod.MOD_ID)
public class RealismoMod {
    public static final String MOD_ID = "realismo";

    public RealismoMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        // Se ejecuta una vez por segundo (20 ticks)
        if (player.tickCount % 20 != 0) return;
        if (player.isCreative() || player.isSpectator()) return;

        applyInjuries(player);
        applyWeight(player);
        applyClimate(player);
        applyEffort(player);
    }

    /** Heridas: con poca vida cojeas y te cuesta minar. */
    private void applyInjuries(ServerPlayer p) {
        float health = p.getHealth();
        if (health <= 6.0f) {
            int amp = health <= 3.0f ? 1 : 0;
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, amp, false, false, true));
        }
        if (health <= 3.0f) {
            p.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, false, false, true));
        }
    }

    /** Peso: llevar el inventario muy lleno te hace mas lento. */
    private void applyWeight(ServerPlayer p) {
        Inventory inv = p.getInventory();
        double weight = 0;
        for (ItemStack stack : inv.items) {
            if (!stack.isEmpty()) {
                weight += stack.getCount() / (double) stack.getMaxStackSize();
            }
        }
        for (ItemStack stack : inv.armor) {
            if (!stack.isEmpty()) weight += 1.0;
        }
        if (weight > 32) {
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, false, true));
        } else if (weight > 24) {
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, false, true));
        }
    }

    /** Clima: el frio y el calor extremos te desgastan mas rapido. */
    private void applyClimate(ServerPlayer p) {
        Biome biome = p.level().getBiome(p.blockPosition()).value();
        float temp = biome.getBaseTemperature();

        boolean warmClothes = p.getItemBySlot(EquipmentSlot.CHEST).is(Items.LEATHER_CHESTPLATE);

        if (temp < 0.15f && !warmClothes) {
            p.causeFoodExhaustion(0.4f); // frio sin abrigo
        } else if (temp >= 1.5f) {
            p.causeFoodExhaustion(0.4f); // calor del desierto
        }

        // Estar bajo la lluvia sin techo enfria un poco
        if (p.level().isRaining() && p.level().canSeeSky(p.blockPosition()) && !warmClothes) {
            p.causeFoodExhaustion(0.1f);
        }
    }

    /** Esfuerzo: correr cansa de verdad. */
    private void applyEffort(ServerPlayer p) {
        if (p.isSprinting()) {
            p.causeFoodExhaustion(0.3f);
        }
    }
}
