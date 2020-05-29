package com.roborock.testbackend.storage;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public String id;
    public String text;
    public String type;
    public String icon;
    public List<Node> children;


    public Node(String id, String root, String type, String icon) {
        this.id = id;
        this.text = root;
        this.type = type;
        this.icon = icon;
        this.children = new ArrayList<>();
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}
