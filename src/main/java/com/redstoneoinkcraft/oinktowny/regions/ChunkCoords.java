package com.redstoneoinkcraft.oinktowny.regions;

import org.bukkit.Chunk;

/**
 * OinkTowny created/started by markb (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ChunkCoords {

    private int x, z;

    public ChunkCoords(int x, int z){
        this.x = x;
        this.z = z;
    }

    public static ChunkCoords createChunkCoords(Chunk chunk){
        return new ChunkCoords(chunk.getX(), chunk.getZ());
    }

    public int getX(){
        return x;
    }

    public int getZ(){
        return z;
    }

    public boolean equals(ChunkCoords otherCoords){
        return x == otherCoords.getX() && z == otherCoords.getZ();
    }

    public String toString(){
        return "(X-" + x + ",Z-" + z + ")";
    }

}
