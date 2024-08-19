package crazypants.enderio.api.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IHideFacades {

    boolean shouldHideFacades(@NotNull ItemStack stack, @NotNull EntityPlayer player);
}
