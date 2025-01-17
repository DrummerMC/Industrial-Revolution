package me.steven.indrev.blockentities.solarpowerplant

import com.google.common.base.Preconditions
import me.steven.indrev.blocks.HeliostatBlock
import me.steven.indrev.registry.IRBlockRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HeliostatBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(IRBlockRegistry.HELIOSTAT_BLOCK_ENTITY, pos, state) {

    var targetBlock: BlockPos = BlockPos.ORIGIN

    var pitch: Float = 0.0f
    var yaw: Float = 0.0f

    companion object {
        fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: HeliostatBlockEntity) {
            //this doesn't work, i need another way to check if there's no obstruction
            //val hit = world!!.raycastBlock(pos.toVec3d().add(0.5, 1.5, 0.5), targetBlock.toVec3d().add(0.5, 0.5, 0.5), pos, cachedState.getOutlineShape(world, pos), cachedState)
            //if (hit !is BlockHitResult || hit.blockPos != targetBlock) return
            if (!world.isSkyVisible(pos.up())) return
            val receiver = world.getBlockEntity(blockEntity.targetBlock) as? SolarReceiverBlockEntity ?: return
            val controller =
                world.getBlockEntity(receiver.controllerPos) as? SolarPowerPlantTowerBlockEntity ?: return
            controller.heliostats++
        }
    }

    override fun writeNbt(tag: NbtCompound) {
        tag.putLong("target", targetBlock.asLong())
        super.writeNbt(tag)
    }
    
    override fun readNbt(tag: NbtCompound) {
        targetBlock = BlockPos.fromLong(tag.getLong("target"))
        yaw = HeliostatBlock.getYaw(pos, targetBlock)
        pitch = HeliostatBlock.getPitch(pos, targetBlock)
        super.readNbt(tag)
    }

    fun sync() {
        Preconditions.checkNotNull(world) // Maintain distinct failure case from below
        check(world is ServerWorld) { "Cannot call sync() on the logical client! Did you check world.isClient first?" }
        (world as ServerWorld).chunkManager.markForUpdate(getPos())
    }
}