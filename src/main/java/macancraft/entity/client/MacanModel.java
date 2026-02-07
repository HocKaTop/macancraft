package macancraft.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class MacanModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart root;

    public MacanModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4F, 0F, -2F, 8F, 12F, 4F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(
            T entity,
            float limbAngle,
            float limbDistance,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {}

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumer vertices,
            int light,
            int overlay,
            int color
    ) {
        root.render(matrices, vertices, light, overlay, color);
    }

}
