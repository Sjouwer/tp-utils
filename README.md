# **TP Utils**

This is a teleport utility mod that should greatly benefit creative players, making it much easier to get around. It includes but is not limited to highly improved versions of the WE compass teleports (/thru & /jumpto). This mod uses Ray Casting to determine the block you’re looking at from a great distance and with extreme precision. 

## **Functions**

**TP Through**

Usage: Keybind or /tpu through

Teleport through walls or pretty much anything else that’s in your way. It’ll teleport you to the exact opposite side of the obstacle without forcing you to the ground.

**TP on Top**

Usage: Keybind or /tpu top

Teleport to the first available free space above the block you’re looking at.

**TP Forward**

Usage: Keybind or /tpu forward

Teleport a certain amount of blocks into the direction you’re looking, including up- and downwards. This teleport however doesn’t let you through blocks and will either teleport you to the full configured distance away or right in front of the first obstacle that’s in your way.

**Chunk TP**

Usage: /tpu chunk \<x> \<z>

Teleport to the coordinates of the entered chunk.

**TP Back**

Usage: Keybind or /tpu back

Teleport back to your previous location. This only works when you used a teleport method from this mod.

## **Configuration (File / Mod Menu)**

**TP Method**

Default: /tp

The tp method the mod should use to teleport you. Any method that uses \<x> \<y> \<z> should work.
For example /tp or /tppos.

**TP Through Range**

Default: 256 (blocks)

From how far should the mod be able to detect the block you’re looking at to teleport you through.

**TP on Top Range**

Default: 256 (blocks)

From how far should the mod be able to detect the block you’re looking at to teleport you on top.

**TP Forward Range**

Default: 100 (blocks)

How far should the mod try to teleport you forward

## **Versions & Dependencies**
**F(at)**

This version already includes some of the dependencies that you’ll need to get most out of this mod.

Included:
- Auto Config Updated API (Is needed to make the config work)
- Cloth Config API Fabric (Required for Auto Config)
- Cotton Client Commands (Is needed to make any of the commands work)
 		
Required:
- Fabric API

Optional:
- Mod Menu (This mod allows you to edit the configs in game)

**L(ight)**

This version doesn’t include any of dependencies and requires you to manually add the following:

Required:
- Fabric API 
- Auto Config Updated API (Is required to make the config work)
- Cloth Config API Fabric (Required for Auto Config)
 		
Optional:
- Cotton Client Commands (Is needed to make any of the commands work)
- Mod Menu (This mod allows you to edit the configs in game)
