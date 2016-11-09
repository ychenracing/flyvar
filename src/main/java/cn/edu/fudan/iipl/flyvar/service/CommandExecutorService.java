package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;

/**
 * 命令执行器服务接口（使用了ProcessBuilder，Linux用bash执行，windows直接执行该命令）
 * 
 * @author yorkchen
 * @since 2016-11-08 15:37:59
 */
public interface CommandExecutorService {

    /**
     * 执行单条命令
     * @param command 命令
     */
    public void execute(String command);

    /**
     * 异步执行单条命令
     * @param command
     */
    public void asyncExecute(String command);

    /**
     * 串行执行多条命令
     * @param commands 命令
     */
    public void executeSerially(List<String> command);

    /**
     * 并发执行多条命令
     * @param commands 命令
     */
    public void executeConcurrently(List<String> commands);

}
