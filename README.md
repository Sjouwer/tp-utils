![icon-small](https://user-images.githubusercontent.com/84018133/117811201-9026d280-b260-11eb-9363-6e344db93bc3.png)

This is a teleport utility mod that should greatly benefit creative players, making it much easier to get around. It includes but is not limited to highly improved and custom versions of the WE compass teleports (/thru & /jumpto). This mod uses Ray Casting to determine the block you’re looking at from a great distance and with extreme precision. 

## **Functions**

**TP Through**  
*Usage: Keybind or /tpu through*  
Teleport through walls or pretty much anything else that’s in your way. It’ll teleport you to the exact opposite side of the obstacle without forcing you to the ground.

**TP on Top**  
*Usage: Keybind or /tpu top*  
Teleport to the first available free space above the block you’re looking at.

**TP Forward**  
*Usage: Keybind or /tpu forward*  
Teleport a certain amount of blocks into the direction you’re looking, including up- and downwards. This teleport however doesn’t let you through blocks and will either teleport you to the full configured distance away or right in front of the first obstacle that’s in your way.

**TP Ground**  
*Usage: /tpu ground*  
Teleport to first block below you, no matter the height you're at.

**Chunk TP**  
*Usage: /tpu chunk \<x> \<z>*  
Teleport to the coordinates of the entered chunk.

**TP Back**  
*Usage: Keybind or /tpu back*  
Teleport back to your previous location. This only works when you used a teleport method from this mod.

## **Configuration (File / Mod Menu)**

**TP Method**  
*Default: /tp*  
The tp method the mod should use to teleport you. Any method that uses \<x> \<y> \<z> should work.
For example /tp or /tppos.

**TP Through Range**  
*Default: 256 (blocks)*  
From how far should the mod be able to detect the block you’re looking at to teleport you through.

**TP on Top Range**  
*Default: 256 (blocks)*  
From how far should the mod be able to detect the block you’re looking at to teleport you on top.

**TP Forward Range**  
*Default: 100 (blocks)*  
How far should the mod try to teleport you forward.

**Allow Crawling**  
*Default: False*  
Allow the mod to teleport you into a 1 block high space.

**Allow Lava**  
*Default: False*  
Allow the mod to teleport you into lava.

**Set Bedrock Limit**  
*Default: True*  
Stop the mod from teleporting you below bedrock / the bottom world limit.

## **Dependencies**

**Included:**  
[Cloth Config API Fabric](https://github.com/shedaniel/cloth-config) (Is required to make the config work)
 		
**Required**:  
[Fabric API](https://github.com/FabricMC/fabric)

**Optional:**  
[Mod Menu](https://github.com/TerraformersMC/ModMenu) (This mod allows you to edit the configs in game)
