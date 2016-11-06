package com.erbre.documentation.appstatus.aspect;

public aspect Interceptor {

    pointcut interceptTarget() : within(com.erbre.documentation..*); 
    
    pointcut interceptService() : execution(public * *(..));
    
    pointcut intercept() : interceptTarget() && interceptService();
    
    
    before() : intercept(){
        System.out.println("Execution start");
        System.out.println(thisJoinPointStaticPart.toShortString());
    }
    after() : intercept(){
        System.out.println("Execution end");
        System.out.println(thisJoinPointStaticPart.toShortString());
    }
}
