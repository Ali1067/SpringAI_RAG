CREATE EXTENSION IF NOT EXISTS vector;
CREATE TABLE IF NOT EXISTS help_pages (id BIGSERIAL PRIMARY KEY,title VARCHAR(200) NOT NULL,url VARCHAR(500) NOT NULL,keywords TEXT,description TEXT NOT NULL);
INSERT INTO help_pages(title,url,keywords,description) VALUES
('Submit Memo','/cab-memo/submit','submit,create,memo','Use this page to create and submit a new memo.'),
('Track Memo','/cab-memo/track','track,status,reference id','Use this page to search for a memo using its Reference ID and view its approval timeline.'),
('Profile','/profile','profile,account','Use this page to view and update your profile information.'),
('Reset Password','/profile/reset-password','password,reset','Use this page to reset your account password.'),
('Approval Timeline','/cab-memo/track/timeline','approval,timeline,steps','The approval timeline shows completed, current, and future approval steps.')
ON CONFLICT DO NOTHING;
