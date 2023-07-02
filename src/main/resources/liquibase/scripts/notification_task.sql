CREATE TABLE notification_task (
                       id PRIMARY KEY,
                       chat_id INT NOT NULL,
                       notification_text VARCHAR(50) NOT NULL,
                       notification_time TIMESTAMP NOT NULL
)

    SET @@session.time_zone="+03:00";