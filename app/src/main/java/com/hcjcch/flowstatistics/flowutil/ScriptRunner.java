package com.hcjcch.flowstatistics.flowutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/16 11:03
 */

public class ScriptRunner extends Thread {
    private final File file;
    private final String script;
    private final StringBuilder res;
    private final boolean asRoot;
    public int exitCode = -1;
    private Process exec;

    /**
     * Creates a new script runner.
     *
     * @param file   temporary script file
     * @param script script to run
     * @param res    response output
     * @param asRoot if true, executes the script as root
     */
    public ScriptRunner(File file, String script, StringBuilder res, boolean asRoot) {
        this.file = file;
        this.script = script;
        this.res = res;
        this.asRoot = asRoot;
    }

    @Override
    public void run() {
        try {
            file.createNewFile();
            final String abspath = file.getAbsolutePath();
            // make sure we have execution permission on the script file
            Runtime.getRuntime().exec("chmod 777 " + abspath).waitFor();
            // Write the script to be executed
            final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            if (new File("/system/bin/sh").exists()) {
                out.write("#!/system/bin/sh\n");
            }
            out.write(script);
            if (!script.endsWith("\n")) out.write("\n");
            out.write("exit\n");
            out.flush();
            out.close();
            if (this.asRoot) {
                // Create the "su" request to run the script
                exec = Runtime.getRuntime().exec("su -c " + abspath);
            } else {
                // Create the "sh" request to run the script
                exec = Runtime.getRuntime().exec("sh " + abspath);
            }
            InputStreamReader r = new InputStreamReader(exec.getInputStream());
            final char buf[] = new char[1024];
            int read;
            // Consume the "stdout"
            while ((read = r.read(buf)) != -1) {
                if (res != null) res.append(buf, 0, read);
            }
            // Consume the "stderr"
            r = new InputStreamReader(exec.getErrorStream());
            while ((read = r.read(buf)) != -1) {
                if (res != null) res.append(buf, 0, read);
            }
            // get the process exit code
            if (exec != null) this.exitCode = exec.waitFor();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            if (res != null) res.append("\nOperation timed-out");
        } catch (Exception ex) {
            ex.printStackTrace();
            if (res != null) res.append("\n" + ex);
        } finally {
            destroy();
        }
    }

    /**
     * Destroy this script runner
     */
    public synchronized void destroy() {
        if (exec != null) exec.destroy();
        exec = null;
    }
}
