package com.cloutropy.platform.funnel;

import java.util.ArrayList;


public class TreeStringResult {
    public ArrayList<Tuple<String,String>> topSegs;
    public ArrayList<ArrayList<TreeStringResult>> subTreeResults;
    public TreeStringResult(){
        topSegs = null;
        subTreeResults = null;
    }

    public ArrayList<ArrayList<Tuple<String,String>>> serialize(){

        ArrayList<ArrayList<Tuple<String,String>>> prev = null;

        if(subTreeResults != null) {
            if(topSegs != null) {
                prev = new ArrayList<>();
                prev.add(topSegs);
            }
            for(ArrayList<TreeStringResult> tree_arr : subTreeResults) {
                ArrayList<ArrayList<Tuple<String, String>>> temp = new ArrayList<>();
                for(TreeStringResult tree : tree_arr) {
                    ArrayList<ArrayList<Tuple<String, String>>> tree_string = tree.serialize();
                    temp.addAll(tree_string);
                }
                prev = cross(prev,temp);
            }
            return prev;
        }
        ArrayList<ArrayList<Tuple<String,String>>> result = new ArrayList<>();
        result.add(topSegs);
        return result;
    }

    private ArrayList<ArrayList<Tuple<String,String>>> cross(ArrayList<ArrayList<Tuple<String,String>>> s1,ArrayList<ArrayList<Tuple<String,String>>> s2) {
        if(s1 == null){
            return s2;
        }
        ArrayList<ArrayList<Tuple<String,String>>> result = new ArrayList<>();
        for(ArrayList<Tuple<String,String>> part1 : s1){
            for(ArrayList<Tuple<String,String>> part2 : s2){
                ArrayList<Tuple<String,String>> one = new ArrayList<>(part1);
                one.addAll(part2);
                result.add(one);
            }
        }
        return result;
    }
}
