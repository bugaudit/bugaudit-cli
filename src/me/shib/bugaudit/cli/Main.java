package me.shib.bugaudit.cli;

import me.shib.bugaudit.core.BugAudit;

public class Main {

    public static void main(String[] args) {
        if (BugAudit.audit().size() > 0) {
            System.exit(1);
        }
    }


}
