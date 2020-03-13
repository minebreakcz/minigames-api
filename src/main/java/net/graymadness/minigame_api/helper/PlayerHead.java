package net.graymadness.minigame_api.helper;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

// GET https://api.mojang.com/users/profiles/minecraft/<username>
// GET https://sessionserver.mojang.com/session/minecraft/profile/<uuid>
@SuppressWarnings("unused")
public class PlayerHead
{
    // fdd40f97-3f5b-457e-9c96-c8d1e9b4ce01
    // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/3cebff90b5c9c59ca3542fd2551ee3999f8e8bffef1085bcc87bf2dcd617fe41"}}}
    public static final PlayerHead Basketball = new PlayerHead("3cebff90b5c9c59ca3542fd2551ee3999f8e8bffef1085bcc87bf2dcd617fe41");
    // b74e58ec-4460-44b9-b0ca-2a1b9f331523
    // {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/8e4a70b7bbcd7a8c322d522520491a27ea6b83d60ecf961d2b4efbbf9f605d"}}}
    public static final PlayerHead SoccerBall = new PlayerHead("8e4a70b7bbcd7a8c322d522520491a27ea6b83d60ecf961d2b4efbbf9f605d");

    /**
     * 110DiePolizei
     * 4cf53d0e-3ac2-40b8-a161-05e8508bf49c
     */
    public static final PlayerHead Guard = new PlayerHead("79813181755bbcff5a6b76babccd5addb18ee9e4c557b71d3bae250b0ba5aea8");
    /**
     * JaiLStream
     * e4b959c2-c076-463f-9630-fa3fbe05e7c1
     */
    public static final PlayerHead Prisoner = new PlayerHead("1793253dc1eda29861cf7497029232b9c12e8a97f2c6ead2128cd2d603deb38f");

    /**
     * MHF_ArrowUp
     * fef039ef-e6cd-4987-9c84-26a3e6134277
     */
    public static final PlayerHead ArrowUp = new PlayerHead("a156b31cbf8f774547dc3f9713a770ecc5c727d967cb0093f26546b920457387");
    /**
     * MHF_ArrowDown
     * 68f59b9b-5b0b-4b05-a9f2-e1d1405aa348
     */
    public static final PlayerHead ArrowDown = new PlayerHead("fe3d755cecbb13a39e8e9354823a9a02a01dce0aca68ffd42e3ea9a9d29e2df2");
    /**
     * MHF_ArrowLeft
     * a68f0b64-8d14-4000-a95f-4b9ba14f8df9
     */
    public static final PlayerHead ArrowLeft = new PlayerHead("f7aacad193e2226971ed95302dba433438be4644fbab5ebf818054061667fbe2");
    /**
     * MHF_ArrowRight
     * 50c8510b-5ea0-4d60-be9a-7d542d6cd156
     */
    public static final PlayerHead ArrowRight = new PlayerHead("d34ef0638537222b20f480694dadc0f85fbe0759d581aa7fcdf2e43139377158");

    /**
     * MHF_Exclamation
     * d3c47f6fae3a45c1ad7ce2c762b03ae6
     */
    public static final PlayerHead Exclamation = new PlayerHead("40b05e699d28b3a278a92d169dca9d57c0791d07994d82de3f9ed4a48afe0e1d");
    /**
     * MHF_Question
     * 606e2ff0ed7748429d6ce1d3321c7838
     */
    public static final PlayerHead Question = new PlayerHead("d34e063cafb467a5c8de43ec78619399f369f4a52434da8017a983cdd92516a0");

    /**
     * MHF_Present1
     * 156b251b-12e0-4829-a130-a61b53ba7720
     */
    public static final PlayerHead Present1 = new PlayerHead("2e10ac999703c735bf7ba6da63f9842d415c5bde07a742f96f3060e11ccfa6c6");
    /**
     * MHF_Present2
     * f1eb7cad-e2c0-4e9e-8aad-1eae21d5fd95
     */
    public static final PlayerHead Present2 = new PlayerHead("407c0d34f90548abf94040e5167c1e07c3427b920f66680d7356e23e11c1a77f");

    /**
     * MHF_Chest
     * 73d4e068-3a6d-4c8b-8f85-3323546955c4
     */
    public static final PlayerHead Chest = new PlayerHead("758305e527fcf08525fdff07e4cd6e79774acdd426cc27327d45df8a5ff86419");

    /**
     * MHF_Cake
     * afb489c4-9fc8-48a4-98f2-dd7bea414c9a
     */
    public static final PlayerHead Cake = new PlayerHead("ec241a597c285e104c271196d785db4cd0110a40c8f8e5d354c5644159567c9d");

    /**
     * MHF_Melon
     * 1c7d9784-47ea-4bf3-bc23-acf260b436e6
     */
    public static final PlayerHead Melon = new PlayerHead("8092d760c453625594662c9fc868152a01a1f6f8d6137fb868da5a95bbd1f58");
    /**
     * MHF_Pumpkin
     * f44d355b-b6ae-4ba8-8e62-ae6441854785
     */
    public static final PlayerHead Pumpkin = new PlayerHead("b162d72d8d576d9bbb81c9aedc690617416dedc95ccf17f794580ca674942a65");
    /**
     * MHF_Cactus
     * 1d9048db-e836-4b9a-a108-55014922f1ae
     */
    public static final PlayerHead Cactus = new PlayerHead("40d4c8e90edef8cde2b89c7b1c65ba02ec3341bad115e00de2199105ff173d94");
    /**
     * MHF_CoconutB
     * 62efa973-f626-4092-aede-57ffbe84ff2b
     */
    public static final PlayerHead CoconutBrown = new PlayerHead("7b3fd7d375bde51828c6f6ec888b2ea86f23fa348db7bdde03f7e007c3c2400b");
    /**
     * MHF_CoconutG
     * 74556fea-28ed-4458-8db2-9a8220da0c12
     */
    public static final PlayerHead CoconutGreen = new PlayerHead("bafb834fac9d1ff2a8061ce917b50eb89ced932fdf50e37b971c75bc8d139f8a");

    /**
     * MHF_TNT
     * d43af93c-c330-4a3d-bab8-ee74234a011a
     */
    public static final PlayerHead Tnt1 = new PlayerHead("f92408fe8d0a3ef5531065e9f566c31aa6eb37484031a46e4466615daf64f705");
    /**
     * MHF_TNT
     * 55e73380-a973-4a52-9bb5-1efa5256125c
     */
    public static final PlayerHead Tnt2 = new PlayerHead("dc75cd6f9c713e9bf43fea963990d142fc0d252974ebe04b2d882166cbb6d294");

    //public static final PlayerHead MHF = new PlayerHead("");


    public final String textureNumber;
    public final String textureUrl;
    public final String textureBase64;

    PlayerHead(@NotNull String textureNumber)
    {
        this.textureNumber = textureNumber;
        this.textureUrl = "http://textures.minecraft.net/texture/" + textureNumber;
        this.textureBase64 = Base64.encodeBase64String(("{\"textures\":{\"SKIN\":{\"url\":\"" + this.textureUrl + "\"}}}").getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    public ItemStack build()
    {
        CraftItemStack craftItemStack = CraftItemStack.asCraftCopy(new ItemStack(Material.PLAYER_HEAD));
        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(craftItemStack);

        NBTTagCompound compound = nmsItemStack.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
            nmsItemStack.setTag(compound);
        }

        {
            // https://minecraft.gamepedia.com/Mob_head#Item_data
            // SkullOwner
            NBTTagCompound skullOwner = new NBTTagCompound();
            {
                // Properties
                NBTTagCompound properties = new NBTTagCompound();
                {
                    // Textures
                    NBTTagList textures = new NBTTagList();
                    {
                        // <texture>
                        NBTTagCompound texture = new NBTTagCompound();
                        {
                            // Value
                            texture.setString("Value", textureBase64);
                        }
                        textures.add(texture);
                    }
                    properties.set("textures", textures);
                }
                skullOwner.set("Properties", properties);

                // Id
                {
                    // length=16
                    byte[] hash = hexStringToByteArray(textureNumber);
                    hash = Arrays.copyOf(hash, 16);

                    hash[6] = (byte) (hash[6] & 0x0f | 0x50); // Major Version = 5
                    //hash[8] = (byte) (hash[8] & 0x3f | 0x80); // Minor Version = 8

                    ByteBuffer bb = ByteBuffer.wrap(hash);

                    UUID uuid = new UUID(bb.getLong(0), bb.getLong(1));
                    skullOwner.setString("Id", uuid.toString());
                }

                // Name
                skullOwner.setString("Name", "§" + textureNumber.substring(0, 10));
            }
            compound.set("SkullOwner", skullOwner);
        }
        nmsItemStack.setTag(compound);

        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static boolean isHead(@Nullable ItemStack is, @NotNull PlayerHead head)
    {
        if(is == null || is.getType() != Material.PLAYER_HEAD)
            return false;

        net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(is);

        NBTTagCompound compound = nmsItemStack.getTag();
        if (compound == null)
            return false;

        // SkullOwner
        NBTTagCompound skullOwner = compound.getCompound("SkullOwner");
        if(skullOwner == null)
            return false;

        // Properties
        NBTTagCompound properties = skullOwner.getCompound("Properties");
        if(properties == null)
            return false;

        // Textures
        NBTBase textures = properties.get("textures");
        if(textures == null || !(textures instanceof NBTTagList))
            return false;
        NBTTagList textured_list = (NBTTagList) textures;
        if(textured_list.isEmpty())
            return false;

        // <texture>
        NBTTagCompound texture = textured_list.getCompound(0);

        // Value
        return texture.hasKey("Value") && head.textureBase64.equals(texture.getString("Value"));
    }
}
