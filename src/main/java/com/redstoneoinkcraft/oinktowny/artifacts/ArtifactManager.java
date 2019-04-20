package com.redstoneoinkcraft.oinktowny.artifacts;

/**
 * OinkTowny created/started by Mark Bacon (Mobkinz78/Dendrobyte)
 * Please do not use or edit without permission!
 * If you have any questions, reach out to me on Twitter: @Mobkinz78
 * §
 */
public class ArtifactManager {

    private static ArtifactManager instance = new ArtifactManager();
    public static ArtifactManager getInstance(){
        return instance;
    }

    private ArtifactManager(){}

    {
        char[][] letters = new char[5][5];
        String[] wordList = {"HELLO", "GOODBYE"};
        for(String word : wordList) {
            for (int i = 0; i < letters.length; i++) {
                for (int j = 0; i < letters[i].length; j++) {

                    if(letters[i][j] == word.charAt(0)){
                        // Do the checks in each direction... I leave this up to you because I don't really want to code this whole thing for you x)
                    }

                }
            }
        }
    }


}
