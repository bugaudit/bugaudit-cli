package me.shib.bugaudit.cli;

import me.shib.bugaudit.BugAudit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class Launcher {

    private static void showHelpMenu() {
        System.out.println("\nList of GIT environment variables to set:");
        for (GitVars var : GitVars.values()) {
            System.out.println("\n" + var);
            for (String description : var.descriptions) {
                System.out.println("\t" + description);
            }
        }
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

    private static boolean isEnvSet(String var) {
        String value = System.getenv(var);
        return value != null && !value.isEmpty();
    }

    private static boolean areAltRequiredVarsSet() {
        return isEnvSet(RequiredVars.BUGAUDIT_TRACKER_API_KEY.toString()) ||
                (isEnvSet(RequiredVars.BUGAUDIT_TRACKER_USERNAME.toString()) &&
                        isEnvSet(RequiredVars.BUGAUDIT_TRACKER_PASSWORD.toString()));
    }

    private static boolean areAllRequiredVariablesSet() {
        for (RequiredVars var : RequiredVars.values()) {
            if (!isEnvSet(var.toString())) {
                if (var == RequiredVars.BUGAUDIT_TRACKER_API_KEY ||
                        var == RequiredVars.BUGAUDIT_TRACKER_USERNAME ||
                        var == RequiredVars.BUGAUDIT_TRACKER_PASSWORD) {
                    if (!areAltRequiredVarsSet()) {
                        System.out.println("Please set " + RequiredVars.BUGAUDIT_TRACKER_API_KEY + " (or) " +
                                RequiredVars.BUGAUDIT_TRACKER_USERNAME + " and " +
                                RequiredVars.BUGAUDIT_TRACKER_PASSWORD);
                        return false;
                    }
                } else {
                    System.out.println("The environment variable " + var + " is not set.");
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        if (!areAllRequiredVariablesSet()) {
            showHelpMenu();
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
                System.out.println("Unable to identify a valid Git repository.");
            }
        }
    }

    private enum GitVars {
        GIT_REPO(new String[]{"The remote URL of the Git repository to run the scan"}),
        GIT_BRANCH(new String[]{"The Git branch or commit over which the scan has to be run [Optional]"}),
        SSH_PRIVATE_KEY(new String[]{"The path to SSH Private Key in local machine (not container)",
                "If not set, the default key will be automatically used",
                "The public key counterpart should be set in your Git Profile/Repo",
                "[GitHub: https://github.com/settings/keys]",
                "[GitLab: https://gitlab.com/profile/keys]"}),
        SSH_PRIVATE_KEY_PASSPHRASE(new String[]{"The passphrase used to access the encrypted SSH private key"});
        private String[] descriptions;

        GitVars(String[] descriptions) {
            this.descriptions = descriptions;
        }
    }

    private enum RequiredVars {
        BUGAUDIT_TRACKER_NAME(new String[]{"The name of the Issue tracker [Jira/Freshrelease]"}),
        BUGAUDIT_TRACKER_ENDPOINT(new String[]{"The URL/endpoint of the Issue tracker",
                "[Example: https://jira.example.com]"}),
        BUGAUDIT_TRACKER_API_KEY(new String[]{"The API token/key to authenticate with the issue tracker",
                "(Not required when username & password are specified)"}),
        BUGAUDIT_TRACKER_USERNAME(new String[]{"The username field of credential to authenticate",
                "with the issue tracker",
                "(Not required when API token/key specified)"}),
        BUGAUDIT_TRACKER_PASSWORD(new String[]{"The password field of credential to authenticate",
                "with the issue tracker",
                "(Not required when API token/key specified)"}),
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
