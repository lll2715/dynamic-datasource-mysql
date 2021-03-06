//package com.example.mysql;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
///**
// * @author: liufeixiang
// * @date: 8/24/21 4:36 PM
// */
//@Aspect
//@Component
//public class DataSourceAop {
//
//    @Pointcut("!@annotation(com.example.mysql.Master) " +
//            "&& (execution(* com.example.mysql.service..*.select*(..)) " +
//            "|| execution(* com.example.mysql.service..*.get*(..)))")
//    public void readPointcut() {
//
//    }
//
//    @Pointcut("@annotation(com.example.mysql.Master) " +
//            "|| execution(* com.example.mysql.service..*.insert*(..)) " +
//            "|| execution(* com.example.mysql.service..*.add*(..)) " +
//            "|| execution(* com.example.mysql.service..*.update*(..)) " +
//            "|| execution(* com.example.mysql.service..*.edit*(..)) " +
//            "|| execution(* com.example.mysql.service..*.delete*(..)) " +
//            "|| execution(* com.example.mysql.service..*.remove*(..))")
//    public void writePointcut() {
//
//    }
//
//    @Before("readPointcut()")
//    public void read() {
//        DBContextHolder.slave();
//    }
//
//    @Before("writePointcut()")
//    public void write() {
//        DBContextHolder.master();
//    }
//
//
//    /**
//     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
//     */
////    @Before("execution(* com.example.mysql.service.impl.*.*(..))")
////    public void before(JoinPoint jp) {
////        String methodName = jp.getSignature().getName();
////
////        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
////            DBContextHolder.slave();
////        }else {
////            DBContextHolder.master();
////        }
////    }
//}
