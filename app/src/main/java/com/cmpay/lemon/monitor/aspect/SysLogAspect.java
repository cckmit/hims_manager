package com.cmpay.lemon.monitor.aspect;

import com.alibaba.fastjson.JSON;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.WebUtils;
import com.cmpay.lemon.monitor.annotation.SysLog;
import com.cmpay.lemon.monitor.bo.SysLogBO;
import com.cmpay.lemon.monitor.service.SysLogService;
import com.cmpay.lemon.monitor.utils.IdGen;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统日志，切面处理类
 *
 * @author hao
 * @date 2018/12/11
 */
@Aspect
@Component
public class SysLogAspect {
    private static Logger logger = LoggerFactory.getLogger(SysLogAspect.class);
    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.cmpay.lemon.monitor.annotation.SysLog)")
    public void logPointCut() {
        // Do nothing
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog( point, time );

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SysLog syslogMethod = signature.getMethod().getAnnotation( SysLog.class );

        SysLog syslogClass = joinPoint.getTarget().getClass().getAnnotation( SysLog.class );

        //注解上的描述
        StringBuilder operation = new StringBuilder();
        if (syslogClass != null) {
            operation.append( syslogClass.value() ).append( "-" );
        }
        if (syslogMethod != null) {
            operation.append( syslogMethod.value() );
        }
        //请求的参数
        String params;
        Object[] args = joinPoint.getArgs();
        params = JSON.toJSONString( args );
        String userAgent = WebUtils.getHttpServletRequest().getHeader( "user-agent" );
        SysLogBO sysLogBO = new SysLogBO();
        String id = IdGen.uuid();
        sysLogBO.setId( id );
        sysLogBO.setUserNo( SecurityUtils.getLoginUserId() );
        sysLogBO.setOperation( operation.toString() );
        sysLogBO.setRequestUri( WebUtils.getHttpServletRequest().getRequestURI() );
        sysLogBO.setTime( time );
        sysLogBO.setIp( WebUtils.resolveClientIP() );
        sysLogBO.setCreateDate( new Date() );
        sysLogBO.setParams( abbr( params, 1000 ) );
        sysLogBO.setUserAgent( userAgent );
        sysLogBO.setMethod( WebUtils.getHttpServletRequest().getMethod() );
        sysLogService.insert( sysLogBO );
    }

    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml( StringEscapeUtils.unescapeHtml4( str ) ).toCharArray()) {
                currentLength += String.valueOf( c ).getBytes( "GBK" ).length;
                if (currentLength <= length - 3) {
                    sb.append( c );
                } else {
                    sb.append( "..." );
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            logger.error( "UnsupportedEncodingException:{}",e.getMessage());
        }
        return "";
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (StringUtils.isBlank( html )) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile( regEx );
        Matcher m = p.matcher( html );
        return m.replaceAll( "" );
    }
}
