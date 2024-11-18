ALTER TABLE tasks
    ADD CONSTRAINT unique_user_task UNIQUE (header, owner_id);