package me.steven.indrev.gui

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import io.github.cottonmc.cotton.gui.impl.client.MouseInputHandler
import net.minecraft.entity.player.PlayerEntity

class IRInventoryScreen<T : SyncedGuiDescription>(controller: T, playerEntity: PlayerEntity) : CottonInventoryScreen<T>(controller, playerEntity) {
}