package me.shib.bugaudit.cli;

import me.shib.bugaudit.BugAudit;

public class Main {
    public static void main(String[] args) throws Exception {
        if (BugAudit.audit().size() > 0) {
            System.exit(1);
        }
    }
}
