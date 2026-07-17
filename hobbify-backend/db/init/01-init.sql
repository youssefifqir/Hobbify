-- Database initialization script
-- This will be run by docker-compose on first startup
-- Note: Database is already created by Docker Compose

-- Create extensions if needed
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Grant permissions to application user
GRANT ALL PRIVILEGES ON DATABASE Hobbify_prod TO Hobbify_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO Hobbify_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO Hobbify_user;

