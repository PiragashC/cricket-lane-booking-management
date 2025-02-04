FROM mysql:8.0

# Set environment variables; these can also be configured in Render's dashboard
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=cricket_lane_booking

# Expose the MySQL port
EXPOSE 3306
