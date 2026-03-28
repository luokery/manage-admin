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
    delete_id BIGINT DEFAULT 0
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
    delete_id BIGINT DEFAULT 0
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

-- 资源表结构 (PostgreSQL)
-- 支持树形结构，包含目录、菜单、按钮三种类型
CREATE TABLE IF NOT EXISTS resources (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    path VARCHAR(200),
    icon VARCHAR(100),
    sort INTEGER DEFAULT 0,
    status INTEGER DEFAULT 1,
    visible BOOLEAN DEFAULT TRUE,
    component VARCHAR(200),
    perms VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INTEGER DEFAULT 1,
    delete_id BIGINT DEFAULT 0,
    CONSTRAINT fk_resource_parent FOREIGN KEY (parent_id) REFERENCES resources(id) ON DELETE SET NULL
);

-- 资源表注释
COMMENT ON TABLE resources IS '资源表（菜单、按钮权限）';
COMMENT ON COLUMN resources.id IS '资源ID';
COMMENT ON COLUMN resources.parent_id IS '父资源ID（树形结构）';
COMMENT ON COLUMN resources.name IS '资源名称';
COMMENT ON COLUMN resources.code IS '资源编码（权限标识）';
COMMENT ON COLUMN resources.type IS '类型：directory-目录，menu-菜单，button-按钮';
COMMENT ON COLUMN resources.path IS '路由路径';
COMMENT ON COLUMN resources.icon IS '图标';
COMMENT ON COLUMN resources.sort IS '排序（升序）';
COMMENT ON COLUMN resources.status IS '状态：1-正常，0-禁用';
COMMENT ON COLUMN resources.visible IS '是否可见';
COMMENT ON COLUMN resources.component IS '组件路径';
COMMENT ON COLUMN resources.perms IS '权限标识（如：system:user:add）';
COMMENT ON COLUMN resources.created_at IS '创建时间';
COMMENT ON COLUMN resources.updated_at IS '更新时间';
COMMENT ON COLUMN resources.version IS '版本号(乐观锁)';
COMMENT ON COLUMN resources.delete_id IS '逻辑删除标记（0-未删除，删除时置为原ID值）';

/*
-- 资源表 updated_at 自动更新触发器
CREATE TRIGGER update_resources_updated_at BEFORE UPDATE ON resources
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
*/
-- 文件信息表结构 (PostgreSQL)
CREATE TABLE IF NOT EXISTS file_info (
    id BIGSERIAL PRIMARY KEY,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL UNIQUE,
    file_path VARCHAR(500) NOT NULL,
    access_url VARCHAR(500),
    file_size BIGINT,
    content_type VARCHAR(100),
    extension VARCHAR(20),
    access_type VARCHAR(20) NOT NULL,
    business_type VARCHAR(50),
    business_id BIGINT,
    uploader_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete_id BIGINT DEFAULT 0
);

-- 文件信息表注释
COMMENT ON TABLE file_info IS '文件信息表';
COMMENT ON COLUMN file_info.id IS '文件ID';
COMMENT ON COLUMN file_info.original_filename IS '原始文件名';
COMMENT ON COLUMN file_info.stored_filename IS '存储文件名（UUID）';
COMMENT ON COLUMN file_info.file_path IS '文件存储路径';
COMMENT ON COLUMN file_info.access_url IS '访问URL';
COMMENT ON COLUMN file_info.file_size IS '文件大小（字节）';
COMMENT ON COLUMN file_info.content_type IS '文件类型（MIME）';
COMMENT ON COLUMN file_info.extension IS '文件扩展名';
COMMENT ON COLUMN file_info.access_type IS '访问类型：public-公开，private-私密';
COMMENT ON COLUMN file_info.business_type IS '业务类型（如：avatar, project-image）';
COMMENT ON COLUMN file_info.business_id IS '关联业务ID';
COMMENT ON COLUMN file_info.uploader_id IS '上传用户ID';
COMMENT ON COLUMN file_info.created_at IS '创建时间';
COMMENT ON COLUMN file_info.updated_at IS '更新时间';
COMMENT ON COLUMN file_info.delete_id IS '逻辑删除标记（0-未删除，删除时置为原ID值）';

/*
-- 文件信息表 updated_at 自动更新触发器
CREATE TRIGGER update_file_info_updated_at BEFORE UPDATE ON file_info
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
*/
-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_delete_id ON users(delete_id);
CREATE INDEX IF NOT EXISTS idx_projects_project_code ON projects(project_code);
CREATE INDEX IF NOT EXISTS idx_projects_status ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_delete_id ON projects(delete_id);
CREATE INDEX IF NOT EXISTS idx_resources_parent_id ON resources(parent_id);
CREATE INDEX IF NOT EXISTS idx_resources_code ON resources(code);
CREATE INDEX IF NOT EXISTS idx_resources_type ON resources(type);
CREATE INDEX IF NOT EXISTS idx_resources_delete_id ON resources(delete_id);
CREATE INDEX IF NOT EXISTS idx_file_info_stored_filename ON file_info(stored_filename);
CREATE INDEX IF NOT EXISTS idx_file_info_access_type ON file_info(access_type);
CREATE INDEX IF NOT EXISTS idx_file_info_business ON file_info(business_type, business_id);
CREATE INDEX IF NOT EXISTS idx_file_info_uploader ON file_info(uploader_id);
CREATE INDEX IF NOT EXISTS idx_file_info_delete_id ON file_info(delete_id);
