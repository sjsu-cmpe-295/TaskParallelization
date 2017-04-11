package com.example;

public class Node {

    private  long id=0;
    private  String nodeType="";
    private  String hostName="";
    private  String ip="";
    private  String status="";

    public String getHostName() {
        return hostName;
    }

    public String getIp() {
        return ip;
    }

    public String getStatus() {
        return status;
    }

    public Node(long id, String nodeType) {
        this.id = id;
        this.nodeType = nodeType;
    }

    public Node(long id, String nodeType, String hostName, String ip, String status) {
        this.id = id;
        this.nodeType = nodeType;
        this.hostName = hostName;
        this.ip = ip;
        this.status = status;
    }
    public long getId() {
        return id;
    }

    public String getNodeType() {
        return nodeType;
    }
}
