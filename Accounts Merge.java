import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, String> emailToName = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        // Initialize emailToName and parent maps
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                parent.put(email, email);
            }
        }

        // Union emails in the same account
        for (List<String> account : accounts) {
            String rootEmail = find(account.get(1), parent);
            for (int i = 2; i < account.size(); i++) {
                String email = account.get(i);
                parent.put(find(email, parent), rootEmail);
            }
        }

        // Group emails by their root email
        Map<String, List<String>> mergedAccounts = new HashMap<>();
        for (String email : emailToName.keySet()) {
            String rootEmail = find(email, parent);
            mergedAccounts.computeIfAbsent(rootEmail, k -> new ArrayList<>()).add(email);
        }

        // Create the final result
        List<List<String>> result = new ArrayList<>();
        for (List<String> emails : mergedAccounts.values()) {
            String rootEmail = emails.get(0);
            String name = emailToName.get(rootEmail);
            emails.sort(String::compareTo);
            emails.add(0, name);
            result.add(emails);
        }

        return result;
    }

    private String find(String email, Map<String, String> parent) {
        if (!email.equals(parent.get(email))) {
            parent.put(email, find(parent.get(email), parent));
        }
        return parent.get(email);
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        List<List<String>> accounts1 = Arrays.asList(
                Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com"),
                Arrays.asList("Mary", "mary@mail.com"),
                Arrays.asList("John", "johnnybravo@mail.com")
        );
        List<List<String>> result1 = solution.accountsMerge(accounts1);
        System.out.println(result1);

        List<List<String>> accounts2 = Arrays.asList(
                Arrays.asList("Gabe", "Gabe0@m.co", "Gabe3@m.co", "Gabe1@m.co"),
                Arrays.asList("Kevin", "Kevin3@m.co", "Kevin5@m.co", "Kevin0@m.co"),
                Arrays.asList("Ethan", "Ethan5@m.co", "Ethan4@m.co", "Ethan0@m.co"),
                Arrays.asList("Hanzo", "Hanzo3@m.co", "Hanzo1@m.co", "Hanzo0@m.co"),
                Arrays.asList("Fern", "Fern5@m.co", "Fern1@m.co", "Fern0@m.co")
        );
        List<List<String>> result2 = solution.accountsMerge(accounts2);
        System.out.println(result2);
    }
}
