package me.shib.bugaudit.cli;

import me.shib.bugaudit.BugAudit;
import me.shib.bugaudit.commons.BugAuditException;
import me.shib.bugaudit.scanner.GitRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Launcher {

    private static final File repoDir = new File("bugaudit-scan-source");
    private static final String gitUrlEnv = "GIT_URL";
    private static final String gitAuthTokenEnv = "GIT_AUTH_TOKEN";
    private static final String[] requiredVariables = {"BUGAUDIT_TRACKER_NAME", "BUGAUDIT_TRACKER_ENDPOINT",
            "BUGAUDIT_TRACKER_API_KEY", "BUGAUDIT_TRACKER_USERNAME", "BUGAUDIT_TRACKER_PASSWORD",
            "BUGAUDIT_PROJECT", "BUGAUDIT_ISSUETYPE"};
    private static final String[] optionalVariables = {"BUGAUDIT_CONFIG", "BUGAUDIT_ASSIGNEE",
            "BUGAUDIT_SCANNER_DIR", "BUGAUDIT_TRACKER_READONLY", "BUGAUDIT_SUBSCRIBERS",
            "BUGAUDIT_SCANNER_CONFIG", "BUGAUDIT_LANG"};

    private static void showHelpMenu() {
        System.out.println("List of REQUIRED environment variables to run:\n");
        System.out.println("GIT_URL\t\t\t\t\t\t -\tThe remote URL of the Git repository to run the scan for");
        System.out.println("GIT_BRANCH\t\t\t\t\t -\tThe Git branch or commit over which the scan has to be run");
        System.out.println("GIT_AUTH_TOKEN\t\t\t\t -\tThe OAuth token to clone the repository " +
                "\n\t\t\t\t\t\t\t\t[GitHub: https://github.com/settings/tokens]" +
                "\n\t\t\t\t\t\t\t\t[GitLab: https://gitlab.com/profile/personal_access_tokens]");
        System.out.println("BUGAUDIT_TRACKER_NAME\t\t -\tThe name of the Issue tracker [Jira/Freshrelease]");
        System.out.println("BUGAUDIT_TRACKER_ENDPOINT\t -\tThe URL/endpoint of the Issue tracker " +
                "\n\t\t\t\t\t\t\t\t[Example: https://jira.example.com]");
        System.out.println("BUGAUDIT_TRACKER_API_KEY\t -\tThe API token/key to authenticate with the issue tracker " +
                "\n\t\t\t\t\t\t\t\t(Not required when username & password are specified)");
        System.out.println("BUGAUDIT_TRACKER_USERNAME\t -\tThe username field of credential to authenticate " +
                "with the issue tracker" +
                "\n\t\t\t\t\t\t\t\t(Not required when API token/key specified)");
        System.out.println("BUGAUDIT_TRACKER_PASSWORD\t -\tThe password field of credential to authenticate " +
                "with the issue tracker" +
                "\n\t\t\t\t\t\t\t\t(Not required when API token/key specified)");
        System.out.println("BUGAUDIT_PROJECT\t\t\t -\tThe Project key or ID where issues are to be tracked [Example: Test]");
        System.out.println("BUGAUDIT_ISSUETYPE\t\t\t -\tThe issue type to track the bugs [Example: Security Bug]");
        System.out.println("\nList of OPTIONAL environment variables that can be used:\n");
        System.out.println("BUGAUDIT_CONFIG\t\t\t\t -\tThe location (URL or local file) of the config file." +
                "\n\t\t\t\t\t\t\t\tThis contains the workflow and other rules the issues have to be maintained." +
                "\n\t\t\t\t\t\t\t\t[Refer default config: https://bugaudit.github.io/bugaudit-cli/bugaudit-config.json]");
        System.out.println("BUGAUDIT_ASSIGNEE\t\t\t -\tThe user to whom the new issues have to be assigned" +
                "[Example: somedude@example.com]");
        System.out.println("BUGAUDIT_SCANNER_DIR\t\t -\t[TRUE/FALSE] Performs a scan inside a specified subdirectory in the repository");
        System.out.println("BUGAUDIT_TRACKER_READONLY\t -\t[TRUE/FALSE] Performs a real scan and mocks the issue tracker updates");
        System.out.println("BUGAUDIT_SUBSCRIBERS\t\t -\tThe users who are required to watch or subscribe to the created issues" +
                "\n\t\t\t\t\t\t\t\t(Might not be available in all issue trackers)");
        System.out.println("BUGAUDIT_SCANNER_CONFIG\t\t -\tAny scanner specific configuration (Not required mostly)");
        System.out.println("BUGAUDIT_LANG\t\t\t\t -\tThe language for which the scanner has to run for" +
                "\n\t\t\t\t\t\t\t\t(BugAudit identifies the language automatically by default anyway)");
    }

    private static boolean areAllRequredVariablesSet() {
        for (String var : requiredVariables) {
            String value = System.getenv(var);
            if (value == null || var.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException, BugAuditException {
        if (!areAllRequredVariablesSet()) {
            showHelpMenu();
        } else {
            String gitUrl = System.getenv(gitUrlEnv);
            String gitAuthToken = System.getenv(gitAuthTokenEnv);
            if (gitUrl != null) {
                GitRepo.cloneRepo(gitUrl, gitAuthToken, repoDir);
            } else {
                File gitDir = new File(".git");
                if (gitDir.exists() && gitDir.isDirectory()) {
                    List<Exception> exceptions = BugAudit.audit();
                    for (Exception e : exceptions) {
                        e.printStackTrace();
                    }
                    if (exceptions.size() > 0) {
                        System.exit(1);
                    } else {
                        System.exit(0);
                    }
                } else {
                    System.out.println("A valid git repository was not found.");
                }
            }
        }
    }

}
