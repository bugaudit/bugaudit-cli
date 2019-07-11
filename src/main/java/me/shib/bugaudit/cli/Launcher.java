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
    private static final String gitBranchEnv = "GIT_BRANCH";
    private static final String gitAuthTokenEnv = "GIT_AUTH_TOKEN";

    private static void showHelpMenu() {
        System.out.println("\nList of GIT environment variables to set:");
        System.out.println("\n" + gitUrlEnv +
                "\n\tThe remote URL of the Git repository to run the scan");
        System.out.println("\n" + gitBranchEnv +
                "\n\tThe Git branch or commit over which the scan has to be run");
        System.out.println("\n" + gitAuthTokenEnv +
                "\n\tThe OAuth token to clone the repository" +
                "\n\t[GitHub: https://github.com/settings/tokens]" +
                "\n\t[GitLab: https://gitlab.com/profile/personal_access_tokens]");
        System.out.println("\nList of REQUIRED environment variables to run:");
        for (RequiredVars var : RequiredVars.values()) {
            System.out.println("\n" + var);
            for (String description : var.descriptions) {
                System.out.println("\t" + description);
            }
        }
        System.out.println("\nList of OPTIONAL environment variables that can be used:");
        for (OptionalVars var : OptionalVars.values()) {
            System.out.println("\n" + var);
            for (String description : var.descriptions) {
                System.out.println("\t" + description);
            }
        }
    }

    private static boolean areAllRequiredVariablesSet() {
        for (RequiredVars var : RequiredVars.values()) {
            String value = System.getenv(var.toString());
            if (value == null || value.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException, BugAuditException {
        if (!areAllRequiredVariablesSet()) {
            showHelpMenu();
        } else {
            String gitUrl = System.getenv(gitUrlEnv);
            String gitBranch = System.getenv(gitBranchEnv);
            String gitAuthToken = System.getenv(gitAuthTokenEnv);
            if (gitUrl != null) {
                GitRepo.cloneRepo(gitUrl, gitBranch, gitAuthToken, repoDir);
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

    private enum RequiredVars {
        BUGAUDIT_TRACKER_NAME(new String[]{"The name of the Issue tracker [Jira/Freshrelease]"}),
        BUGAUDIT_TRACKER_ENDPOINT(new String[]{"The URL/endpoint of the Issue tracker", "[Example: https://jira.example.com]"}),
        BUGAUDIT_TRACKER_API_KEY(new String[]{"The API token/key to authenticate with the issue tracker", "(Not required when username & password are specified)"}),
        BUGAUDIT_TRACKER_USERNAME(new String[]{"The username field of credential to authenticate", "with the issue tracker", "(Not required when API token/key specified)"}),
        BUGAUDIT_TRACKER_PASSWORD(new String[]{"The password field of credential to authenticate", "with the issue tracker", "(Not required when API token/key specified)"}),
        BUGAUDIT_PROJECT(new String[]{"The Project key or ID where issues are to be tracked [Example: TEST]"}),
        BUGAUDIT_ISSUETYPE(new String[]{"The issue type to track the bugs [Example: Security Bug]"});

        private String[] descriptions;

        RequiredVars(String[] descriptions) {
            this.descriptions = descriptions;
        }
    }

    private enum OptionalVars {
        BUGAUDIT_CONFIG(new String[]{"The location (URL or local file) of the config file.",
                "This contains the workflow and other rules the issues have to be maintained.",
                "[Refer default config: https://bugaudit.github.io/bugaudit-cli/bugaudit-config.json]"}),
        BUGAUDIT_ASSIGNEE(new String[]{"The user to whom the new issues have to be assigned",
                "[Example: somedude@example.com]"}),
        BUGAUDIT_SCANNER_DIR(new String[]{"[TRUE/FALSE] Performs a scan inside a specified subdirectory in the repository"}),
        BUGAUDIT_TRACKER_READONLY(new String[]{"[TRUE/FALSE] Performs a real scan and mocks the issue tracker updates"}),
        BUGAUDIT_SUBSCRIBERS(new String[]{"Comma separated values users who are required to watch or subscribe to the created issues",
                "(Might not be available in all issue trackers)"}),
        BUGAUDIT_SCANNER_CONFIG(new String[]{"Any scanner specific configuration (Not required mostly)"}),
        BUGAUDIT_LANG(new String[]{"The language for which the scanner has to run for",
                "[Example: Ruby, JavaScript]",
                "(BugAudit identifies the language automatically by default anyway)"});

        private String[] descriptions;

        OptionalVars(String[] descriptions) {
            this.descriptions = descriptions;
        }
    }

}
