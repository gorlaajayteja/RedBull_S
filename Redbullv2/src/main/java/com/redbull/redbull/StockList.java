package com.redbull.redbull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StockList {
	public StockList() {}
	Set<String> slhitstock = new HashSet<>();
	public static List<String> buyList = new ArrayList<>();
	public static List<String> sellList = new ArrayList<>();
	static HashSet<String> buyfaildstocks = new HashSet<>();
	static HashSet<String> sellfaildstocks = new HashSet<>();
	static HashSet<String> momentlessstock = new HashSet<>();
	
	static HashSet<String> allstocks = new HashSet<>(Arrays.asList(
            "ADANIENT", "ADANIPORTS", "APOLLOHOSP", "ASIANPAINT", "AXISBANK",
            "BPCL", "BAJAJ-AUTO", "BAJFINANCE", "BAJAJFINSV", "BHARTIARTL",
            "BRITANNIA", "CIPLA", "COALINDIA", "DIVISLAB", "DRREDDY", 
            "EICHERMOT", "GRASIM", "HCLTECH", "HDFCBANK", "HDFCLIFE", 
            "HEROMOTOCO", "HINDUNILVR", "HINDALCO", "ICICIBANK", "INDUSINDBK",
            "INFY", "ITC", "JSWSTEEL", "KOTAKBANK", "LT", "LTIM", 
            "M&M", "MARUTI", "NESTLEIND", "NTPC", "ONGC", 
            "POWERGRID", "RELIANCE", "SBILIFE", "SHRIRAMFIN", "SBIN", 
            "SUNPHARMA", "TATACONSUM", "TATAMOTORS", "TATASTEEL", "TCS",
            "TECHM", "TITAN", "ULTRACEMCO", "WIPRO"
        ));

//	public static boolean placedsellstocks(String stock) {
//		if(StockList.placedsellstocks(stock)){
//			return true;
//		}
//		return false;
//	}
//	public static boolean placedbuytocks() {
//		if(StockList.placedsellstocks(stock)){
//			return true;
//		}
//		return false;
//	}
//	public static boolean removeSellStock(String stock) {
//		if(StockList.placedsellstocks(stock)){
//			return true;
//		}
//		return false;
//	}
}
