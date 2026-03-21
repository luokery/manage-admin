-- 用户表结构 (PostgreSQL)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL DEFAULT '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    age INTEGER,
    role VARCHAR(20) DEFAULT 'user',
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1,
    delete_id BIGINT DEFAULT 1
);

-- 用户表注释
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户ID';
COMMENT ON COLUMN users.username IS '用户名';
COMMENT ON COLUMN users.password IS '密码';
COMMENT ON COLUMN users.email IS '邮箱';
COMMENT ON COLUMN users.phone IS '手机号';
COMMENT ON COLUMN users.age IS '年龄';
COMMENT ON COLUMN users.role IS '角色';
COMMENT ON COLUMN users.status IS '状态: 1-正常, 0-禁用';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.updated_at IS '更新时间';
COMMENT ON COLUMN users.version IS '版本号(乐观锁)';
COMMENT ON COLUMN users.delete_id IS '逻辑删除标记';

/*
-- 用户表 updated_at 自动更新触发器
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
*/
-- 项目表结构 (PostgreSQL)
CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    project_code VARCHAR(50) NOT NULL UNIQUE,
    project_name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    status INTEGER DEFAULT 1,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1,
    delete_id BIGINT DEFAULT 1
);

-- 项目表注释
COMMENT ON TABLE projects IS '项目表';
COMMENT ON COLUMN projects.id IS '项目ID';
COMMENT ON COLUMN projects.project_code IS '项目编号';
COMMENT ON COLUMN projects.project_name IS '项目名称';
COMMENT ON COLUMN projects.description IS '项目描述';
COMMENT ON COLUMN projects.image_url IS '项目图片URL';
COMMENT ON COLUMN projects.status IS '状态: 0-已暂停, 1-进行中, 2-已完成';
COMMENT ON COLUMN projects.start_date IS '开始日期';
COMMENT ON COLUMN projects.end_date IS '结束日期';
COMMENT ON COLUMN projects.created_at IS '创建时间';
COMMENT ON COLUMN projects.updated_at IS '更新时间';
COMMENT ON COLUMN projects.version IS '版本号(乐观锁)';
COMMENT ON COLUMN projects.delete_id IS '逻辑删除标记';

/*
-- 项目表 updated_at 自动更新触发器
CREATE TRIGGER update_projects_updated_at BEFORE UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
*/
-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_delete_id ON users(delete_id);
CREATE INDEX IF NOT EXISTS idx_projects_project_code ON projects(project_code);
CREATE INDEX IF NOT EXISTS idx_projects_status ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_delete_id ON projects(delete_id);
