/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Prevents players from propelling themselves forward by shooting themselves.
 */
public class ModuleDisableBowBoost extends OCMModule {

    public ModuleDisableBowBoost(final OCMMain plugin) {
        super(plugin, "disable-bow-boost");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof final Player player)) return;

        if (!isEnabled(player.getWorld())) return;

        if (!(e.getDamager() instanceof final Arrow arrow)) return;

        final ProjectileSource shooter = arrow.getShooter();

        if (shooter instanceof final Player shootingPlayer) {
            if (player.getUniqueId().equals(shootingPlayer.getUniqueId())) {
                e.setCancelled(true);
                debug("We cancelled your bow boost", player);
            }
        }
    }
}