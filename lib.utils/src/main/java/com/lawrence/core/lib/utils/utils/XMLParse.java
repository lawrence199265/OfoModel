//package com.lawrence.core.lib.utils.utils;
//
//import android.util.Log;
//import android.util.Xml;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.nexd.app.sample.Mall;
//
///**
// * Created by wangxu on 17/2/6.
// */
//
//public class XMLParse {
//
//
//    public static List<Mall> parse(InputStream inputStream) throws IOException, XmlPullParserException {
//        List<Mall> mallList = new ArrayList<>();
//
////        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
////        String line;
////        StringBuilder builder = new StringBuilder();
////        while ((line = reader.readLine()) != null) {
////            builder.append(line)
////                    .append("\n");
////        }
//
//
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(inputStream, "UTF-8");
//
//        int eventType = parser.getEventType();
//
//
//        Mall mall = null;
//        ArrayList<Mall.Floor> floors = null;
//        Mall.Floor floor = null;
//
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            switch (eventType) {
//
//                case XmlPullParser.START_DOCUMENT:
//                    Log.d("xml_pull_parser", "开始解析 XML");
//                    break;
//
//                case XmlPullParser.START_TAG:
//                    if (parser.getName().equals("mall")) {
//                        mall = new Mall();
//                    } else if (parser.getName().equals("mallId")) {
//                        eventType = parser.next();
//                        mall.setMallId(parser.getText());
//                    } else if (parser.getName().equals("mallName")) {
//                        eventType = parser.next();
//                        mall.setMallName(parser.getText());
//                    } else if (parser.getName().equals("floors")) {
//                        eventType = parser.next();
//                        floors = new ArrayList<>();
//                    } else if (parser.getName().equals("floor")) {
//                        eventType = parser.next();
//                        floor = new Mall.Floor();
//                    } else if (parser.getName().equals("floorId")) {
//                        eventType = parser.next();
//                        floor.setFloorId(parser.getText());
//                    } else if (parser.getName().equals("floorName")) {
//                        eventType = parser.next();
//                        floor.setFloorName(parser.getText());
//                    }
//                    break;
//
//                case XmlPullParser.END_TAG:
//                    if (parser.getName().equals("floor")) {
//                        floors.add(floor);
//                    } else if (parser.getName().equals("floors")) {
//                        mall.setFloors(floors);
//                    } else if (parser.getName().equals("mall")) {
//                        mallList.add(mall);
//                        Log.d("xml_pull_parser", "解析完成一栋楼");
//                    }
//                    break;
//
//            }
//            eventType = parser.next();
//        }
//
//        return mallList;
//    }
//}
