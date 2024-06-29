package a2;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

public class AdminControlPanel extends JFrame 
{

    private TwitterService twitterService;
    private JTree userGroupTree;
    private DefaultTreeModel treeModel;
    private JTextArea userIdField;
    private JTextArea groupIdField;

    public AdminControlPanel() 
    {
        twitterService = TwitterService.getInstance();
        initializeUI();
    }

    private void initializeUI() 
    {
        setTitle("Admin Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Initialize tree
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
        userGroupTree = new JTree(rootNode);
        treeModel = (DefaultTreeModel) userGroupTree.getModel();
        
        userGroupTree.setCellRenderer(new GroupTreeCellRenderer());

        JScrollPane treeScrollPane = new JScrollPane(userGroupTree);
        treeScrollPane.setPreferredSize(new Dimension(250, 400));

        mainPanel.add(treeScrollPane, BorderLayout.CENTER);

        // Control panel for user ID, group ID, and buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Panel for User ID
        JPanel userIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextArea(1, 15);
        userIdPanel.add(userIdLabel);
        userIdPanel.add(userIdField);

        // Panel for Group ID
        JPanel groupIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel groupIdLabel = new JLabel("User Group ID:");
        groupIdField = new JTextArea(1, 15);
        groupIdPanel.add(groupIdLabel);
        groupIdPanel.add(groupIdField);

        // Panel for buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> {
            String userId = userIdField.getText().trim();
            if (!userId.isEmpty()) {
                twitterService.addUser(userId);
                updateTreeView();
                userIdField.setText("");
            }
        });
        buttonsPanel.add(addUserButton);

        JButton addUserGroupButton = new JButton("Add User Group");
        addUserGroupButton.addActionListener(e -> {
            String groupId = groupIdField.getText().trim();
            if (!groupId.isEmpty()) {
                twitterService.addUserToGroup(groupId);
                updateTreeView();
                groupIdField.setText("");
            }
        });
        buttonsPanel.add(addUserGroupButton);

        // Add Open User View button
        JButton openUserViewButton = new JButton("Open User View");
        openUserViewButton.addActionListener(e -> {
            TreePath path = userGroupTree.getSelectionPath();
            if (path != null) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                Object selectedObject = selectedNode.getUserObject();
                if (selectedObject instanceof String) {
                    String userId = (String) selectedObject;
                    openUserView(userId);
                }
            }
        });

        controlPanel.add(userIdPanel);
        controlPanel.add(groupIdPanel);
        controlPanel.add(buttonsPanel);
        controlPanel.add(openUserViewButton); // Corrected placement of openUserViewButton

        mainPanel.add(controlPanel, BorderLayout.WEST);

        // Buttons for statistics
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton totalUsersButton = new JButton("Total Users");
        totalUsersButton.addActionListener(e -> {
            int totalUsers = twitterService.getTotalUsers();
            JOptionPane.showMessageDialog(null, "Total Users: " + totalUsers);
        });
        statPanel.add(totalUsersButton);

        JButton totalGroupsButton = new JButton("Total Groups");
        totalGroupsButton.addActionListener(e -> {
            int totalGroups = twitterService.getTotalGroups();
            JOptionPane.showMessageDialog(null, "Total Groups: " + totalGroups);
        });
        statPanel.add(totalGroupsButton);

        JButton totalTweetsButton = new JButton("Total Tweets");
        totalTweetsButton.addActionListener(e -> {
            int totalTweets = twitterService.getTotalTweets();
            JOptionPane.showMessageDialog(null, "Total Tweets: " + totalTweets);
        });
        statPanel.add(totalTweetsButton);

        JButton positivePercentageButton = new JButton("Positive Tweets %");
        positivePercentageButton.addActionListener(e -> {
            double positivePercentage = twitterService.getPositiveTweetPercentage();
            JOptionPane.showMessageDialog(null, "Positive Tweets Percentage: " + positivePercentage + "%");
        });
        statPanel.add(positivePercentageButton);

        mainPanel.add(statPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(mainPanel);

        pack();
    }

    private void updateTreeView() 
    {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");
        populateTree(rootNode, "Root");
        treeModel.setRoot(rootNode);
        treeModel.reload(); // Refresh the tree model to reflect changes
        
    }

    private void populateTree(DefaultMutableTreeNode node, String groupId) 
    {
        UserGroup group = twitterService.getGroup(groupId);
        if (group != null) 
        {
            for (Object member : group.getMembers())
            {
                if (member instanceof User) 
                {
                    User user = (User) member;
                    DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user.getUserId());
                    node.add(userNode);
                } 
                else if (member instanceof UserGroup) 
                {
                    UserGroup subGroup = (UserGroup) member;
                    DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(subGroup.getGroupId());
                    groupNode.setAllowsChildren(true); // Enable to display as folder icon
                    node.add(groupNode);
                    populateTree(groupNode, subGroup.getGroupId());
                }
            }
        }
    }

    private void openUserView(String userId) 
    {
        UserView userView = new UserView(userId, twitterService);
        userView.setVisible(true);
        userView.addNewsFeedListener((userId1, updatedNewsFeed) -> 
        {
            // Handle news feed update in AdminControlPanel directly
            // For demonstration, we print the update here
            System.out.println("News feed updated for user " + userId1 + ": " + updatedNewsFeed);
        });
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> {
            AdminControlPanel adminPanel = new AdminControlPanel();
            adminPanel.setVisible(true);
        });
    }
}
