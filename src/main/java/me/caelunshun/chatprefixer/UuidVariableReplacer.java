package me.caelunshun.chatprefixer;

import org.bukkit.entity.Player;

public class UuidVariableReplacer implements VariableReplacer {
  @Override
  public String apply(Player sender) {
    return sender.getUniqueId().toString();
  }
}
