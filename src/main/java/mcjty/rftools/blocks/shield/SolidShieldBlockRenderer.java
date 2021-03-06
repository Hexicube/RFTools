package mcjty.rftools.blocks.shield;

import mcjty.rftools.render.DefaultISBRH;
import mcjty.rftools.render.TesseleratorAccessHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class SolidShieldBlockRenderer extends DefaultISBRH {
    @Override
    public int getRenderId() {
        return SolidShieldBlock.RENDERID_SHIELDBLOCK;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        ShieldBlockTileEntity shieldBlockTileEntity = (ShieldBlockTileEntity) world.getTileEntity(x, y, z);
        Block camoBlock = shieldBlockTileEntity.getBlock();
        if (camoBlock == null) {
            renderShield(world, x, y, z, block, shieldBlockTileEntity);
        } else {
            try {
                int addedVertices = TesseleratorAccessHelper.getAddedVertices(Tessellator.instance);
                boolean rc = renderer.renderBlockByRenderType(camoBlock, x, y, z);
                if (!rc) {
                    return false;
                }
                int newAddedVertices = TesseleratorAccessHelper.getAddedVertices(Tessellator.instance);
                return addedVertices != newAddedVertices;
            } catch (Exception e) {
                // Ignore this error. Nothing is rendered  in this case.
                return false;
            }
        }
        return true;
    }

    private void renderShield(IBlockAccess world, int x, int y, int z, Block block, ShieldBlockTileEntity shieldBlockTileEntity) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorRGBA_I(shieldBlockTileEntity.getShieldColor(), 180);
//        tessellator.setColorRGBA(150, 255, 200, 180);
        tessellator.addTranslation(x, y, z);
        tessellator.setBrightness(240);

        IIcon[] icons = ((SolidShieldBlock) block).getIcons();
        IIcon icon;

        icon = icons[(z & 0x1) * 2 + (x & 0x1)];
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.DOWN);
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.UP);

        icon = icons[(y & 0x1) * 2 + ((x+z) & 0x1)];
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.NORTH);
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.SOUTH);
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.WEST);
        addSideConditionally(world, x, y, z, block, tessellator, icon, ForgeDirection.EAST);

        tessellator.addTranslation(-x, -y, -z);
    }

}
