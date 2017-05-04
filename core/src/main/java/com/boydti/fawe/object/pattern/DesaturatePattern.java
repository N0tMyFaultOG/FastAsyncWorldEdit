package com.boydti.fawe.object.pattern;

import com.boydti.fawe.util.TextureUtil;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.AbstractPattern;

public class DesaturatePattern extends AbstractPattern{
    private final TextureUtil util;
    private final Extent extent;
    private final double value;

    public DesaturatePattern(Extent extent, TextureUtil util, double value) {
        this.extent = extent;
        this.util = util;
        this.value = Math.max(0, Math.min(1, value));
    }
    @Override
    public BaseBlock apply(Vector position) {
        BaseBlock block = extent.getBlock(position);
        int color = util.getColor(block);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        int alpha = (color >> 24) & 0xFF;
        double l = 0.3f * r + 0.6f * g + 0.1f * b;
        int red = (int) (r + value * (l - r));
        int green = (int) (g + value * (l - g));
        int blue = (int) (b + value * (l - b));
        int newColor = (alpha << 24) + (red << 16) + (green << 8) + (blue << 0);
        return util.getNearestBlock(newColor);
    }

    @Override
    public boolean apply(Extent extent, Vector setPosition, Vector getPosition) throws WorldEditException {
        BaseBlock block = extent.getBlock(getPosition);
        int color = util.getColor(block);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        int alpha = (color >> 24) & 0xFF;
        double l = 0.3f * r + 0.6f * g + 0.1f * b;
        int red = (int) (r + value * (l - r));
        int green = (int) (g + value * (l - g));
        int blue = (int) (b + value * (l - b));
        int newColor = (alpha << 24) + (red << 16) + (green << 8) + (blue << 0);
        if (newColor == color) {
            return false;
        }
        BaseBlock newBlock = util.getNextNearestBlock(newColor);
        if (block.equals(newBlock)) {
            return false;
        }
        return extent.setBlock(setPosition, newBlock);
    }
}