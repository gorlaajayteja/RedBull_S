//package com.redbull.redbull;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class DashboardController {
//
//    private final StringBuilder logs = new StringBuilder();
//
//    @GetMapping("/")
//    public String home(Model model) {
//        model.addAttribute("logContent", logs.toString());
//        return "dashboard";
//    }
//
//    @PostMapping("/start")
//    public String startStrategy(Model model) {
//        logs.append("🔄 Strategy Started...\n");
//
//        new Thread(() -> {
//            try {
//                StrategyRunner.runADX_FABStrategy();
//                logs.append("✅ Strategy Completed\n");
//            } catch (Exception e) {
//                logs.append("❌ Error: ").append(e.getMessage()).append("\n");
//            }
//        }).start();
//
//        model.addAttribute("logContent", logs.toString());
//        return "dashboard";
//    }
//
//    @PostMapping("/stop")
//    public String stopStrategy(Model model) {
//        logs.append("🛑 Stop not implemented (manual interrupt required for now).\n");
//        model.addAttribute("logContent", logs.toString());
//        return "dashboard";
//    }
//}
