package com.enderio.core.common.interfaces;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IOverlayRenderAware {

    public void renderItemOverlayIntoGUI(@NotNull ItemStack stack, int xPosition, int yPosition);

}
