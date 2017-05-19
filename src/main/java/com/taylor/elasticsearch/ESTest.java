package com.taylor.elasticsearch;

import sun.applet.Main;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/5/19 10:39r
 */
public class ESTest {
    public static void main(String[] args) {
        ElasticsearchService esService=new ElasticsearchService("elasticsearch","192.168.186.130","1300","es","test");
        System.out.println(esService);
    }
}
