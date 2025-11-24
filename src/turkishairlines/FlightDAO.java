package turkishairlines;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FlightDAO {

    public boolean insertFlight(String flightId, String passengers, String flightClass) throws SQLException {
        String sql = "INSERT INTO ticket (Flight_id, Number_of_Passengers, Flight_Class) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flightId);
            ps.setString(2, passengers);
            ps.setString(3, flightClass);

            return ps.executeUpdate() > 0;
        }
    }
}
