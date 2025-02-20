package org.example.auth.services;

import org.example.auth.models.Reply;
import org.example.auth.utils.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReplyService {

    private DatabaseService databaseService;

    public ReplyService() {
        this.databaseService = new DatabaseService();
    }

    /**
     * Adds a new reply to a post.
     *
     * @param reply The reply to add.
     * @return True if the reply was added successfully, false otherwise.
     */
    public boolean addReply(Reply reply) {
        String query = "INSERT INTO replies (post_id, user_id, content) VALUES (?, ?, ?)";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reply.getPostId());
            stmt.setInt(2, reply.getUserId());
            stmt.setString(3, reply.getContent());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error while adding reply: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all replies for a given post ID.
     *
     * @param postId The ID of the post.
     * @return A list of replies associated with the post.
     */
    public List<Reply> getRepliesByPostId(int postId) {
        List<Reply> replies = new ArrayList<>();
        String query = "SELECT * FROM replies WHERE post_id = ? ";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reply reply = mapResultSetToReply(rs);
                replies.add(reply);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving replies: " + e.getMessage());
        }

        return replies;
    }

    /**
     * Retrieves a single reply by its ID.
     *
     * @param replyId The ID of the reply.
     * @return The reply if found, otherwise null.
     */
    public Reply getReplyById(int replyId) {
        String query = "SELECT * FROM replies WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, replyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReply(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving reply by ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Deletes a reply by ID.
     *
     * @param replyId The ID of the reply to delete.
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteReply(int replyId) {
        String query = "DELETE FROM replies WHERE id = ?";

        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, replyId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error while deleting reply: " + e.getMessage());
            return false;
        }
    }

    /**
     * Maps a ResultSet row to a Reply object.
     *
     * @param rs The ResultSet containing reply data.
     * @return A Reply object.
     * @throws SQLException If an SQL error occurs.
     */
    private Reply mapResultSetToReply(ResultSet rs) throws SQLException {
        Reply reply = new Reply();
        reply.setId(rs.getInt("id"));
        reply.setPostId(rs.getInt("post_id"));
        reply.setUserId(rs.getInt("user_id"));
        reply.setContent(rs.getString("content"));
        return reply;
    }
}
