package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sjsu.cmpe.B295.raspberrypi.node.TaskParallelizeApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {

    private static final String template = "Hello, %s!";
//    private static final String[] status = {"slave","master"};

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/getNodeDetails")
    public List<Node> getNodeDetails() {
        System.out.println("getNodeDetails accessed");
        List l=new ArrayList<Node>();
        l.add(new Node(counter.incrementAndGet(), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)],"rasp1","127.168.0.34","Idle"));
        l.add(new Node(counter.incrementAndGet(), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)],"rasp2","127.168.0.05","Running"));
        l.add(new Node(counter.incrementAndGet(), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)],"rasp3","127.168.0.21","Idle"));
        l.add(new Node(counter.incrementAndGet(), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)],"rasp4","127.168.0.17","Running"));
        return l;
    }

}
