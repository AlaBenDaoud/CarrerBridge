package org.example.auth.services;

import org.example.auth.models.Post;
import org.example.auth.utils.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    private DatabaseService databaseService;

    public PostService() {
        this.databaseService = new DatabaseService();
    }

    /**
     * Adds a new post to the database.
     *
     * @param post The post to add.
     * @return True if the post was added successfully, false otherwise.
     */
    public boolean addPost(Post post) {
        String query = "INSERT INTO posts (user_id, content, like_count, dislike_count, created_at, image_path, pdf_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getContent());
            stmt.setInt(3, post.getLikeCount());
            stmt.setInt(4, post.getDislikeCount());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.setString(6, post.getImagePath()); // Image path
            stmt.setString(7, post.getPdfPath());   // PDF path

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error while adding the post: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all posts from the database in reverse chronological order.
     *
     * @return A list of all posts, newest first.
     */
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts ORDER BY created_at DESC"; // Order by newest first

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Post post = mapResultSetToPost(rs);
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving posts: " + e.getMessage());
        }

        return posts;
    }

    /**
     * Updates the like count for a post.
     *
     * @param postId The ID of the post to update.
     * @param increment Whether to increment (true) or decrement (false) the like count.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateLikeCount(int postId, boolean increment) {
        String query = "UPDATE posts SET like_count = like_count " + (increment ? "+ 1" : "- 1") + " WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error while updating like count: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the dislike count for a post.
     *
     * @param postId The ID of the post to update.
     * @param increment Whether to increment (true) or decrement (false) the dislike count.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateDislikeCount(int postId, boolean increment) {
        String query = "UPDATE posts SET dislike_count = dislike_count " + (increment ? "+ 1" : "- 1") + " WHERE id = ?";
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, postId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error while updating dislike count: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all posts by a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of posts by the user.
     */
    public List<Post> getPostsByUserId(int userId) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts WHERE user_id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Post post = mapResultSetToPost(rs);
                posts.add(post);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving posts by userId: " + e.getMessage());
        }

        return posts;
    }

    /**
     * Maps a ResultSet row to a Post object.
     *
     * @param rs The ResultSet containing the post data.
     * @return A Post object.
     * @throws SQLException If an SQL error occurs.
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setUserId(rs.getInt("user_id"));
        post.setContent(rs.getString("content"));
        post.setLikeCount(rs.getInt("like_count"));
        post.setDislikeCount(rs.getInt("dislike_count"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setImagePath(rs.getString("image_path")); // Retrieve image path
        post.setPdfPath(rs.getString("pdf_path"));     // Retrieve PDF path
        return post;
    }
}