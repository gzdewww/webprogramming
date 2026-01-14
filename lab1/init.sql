-- Удаляем старые объекты (если существуют)
DROP TABLE IF EXISTS track CASCADE;
DROP TABLE IF EXISTS album CASCADE;
DROP TABLE IF EXISTS artist CASCADE;
DROP SEQUENCE IF EXISTS seq_artist;
DROP SEQUENCE IF EXISTS seq_album;
DROP SEQUENCE IF EXISTS seq_track;

-- Создаём последовательности
CREATE SEQUENCE seq_artist START 1 INCREMENT 1;
CREATE SEQUENCE seq_album START 1 INCREMENT 1;
CREATE SEQUENCE seq_track START 1 INCREMENT 1;

-- Таблица исполнителей
CREATE TABLE artist (
    artist_id INT PRIMARY KEY DEFAULT nextval('seq_artist'),
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица альбомов
CREATE TABLE album (
    album_id INT PRIMARY KEY DEFAULT nextval('seq_album'),
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    artist_id INT NOT NULL,
    CONSTRAINT fk_album_artist FOREIGN KEY (artist_id) REFERENCES artist(artist_id) ON DELETE CASCADE
);

-- Таблица композиций
CREATE TABLE track (
    track_id INT PRIMARY KEY DEFAULT nextval('seq_track'),
    title VARCHAR(100) NOT NULL,
    duration INTERVAL NOT NULL,
    album_id INT NOT NULL,
    CONSTRAINT fk_track_album FOREIGN KEY (album_id) REFERENCES album(album_id) ON DELETE CASCADE
);


-- ЗАПОЛНЕНИЕ ДАННЫМИ (адаптировано под задания)
-- =============================

-- Исполнители (есть Bond для задания 5, фамилии с "e" для задания 3)
INSERT INTO artist (artist_id, name) VALUES
(1111111, 'Benson'),
(2222222, 'Keller'),
(nextval('seq_artist'), 'Bond'),
(nextval('seq_artist'), 'Metallica'),
(nextval('seq_artist'), 'Imagine Dragons');

-- Альбомы (у Bond будет альбом)
INSERT INTO album (title, genre, artist_id) VALUES
('Strings Attached', 'Classical', 1),
('Master of Puppets', 'Metal', 2),
('Evolve', 'Alternative Rock', 3);

-- Композиции (есть короткие и длинные, для проверки всех условий)
INSERT INTO track (title, duration, album_id) VALUES
('Girl', '00:03:40', 1),
('Symphony', '00:07:30', 1),
('Battery', '00:05:12', 2),
('Master of Puppets', '00:08:36', 2),
('Believer', '00:03:24', 3),
('Thunder', '00:03:07', 3),
('Walking the Wire', '00:04:30', 3);