-- 1. Название композиции и длительность в одном столбце Song Details
SELECT title || ', ' || duration AS "Song Details"
FROM track;

-- 2. Композиции вне диапазона 5–10 минут, сортировка по убыванию
-- Способ 1
SELECT title, duration
FROM track
WHERE duration NOT BETWEEN '00:05:00' AND '00:10:00'
ORDER BY duration DESC;

-- Способ 2
SELECT title, duration
FROM track
WHERE duration < '00:05:00' OR duration > '00:10:00'
ORDER BY duration DESC;

-- 3. Исполнители, у которых вторая буква 'е' и ID = 1111111 или 2222222
-- Способ 1
SELECT name
FROM artist
WHERE name LIKE '_e%' 
  AND artist_id IN (1111111, 2222222);

-- Способ 2
SELECT name
FROM artist
WHERE name LIKE '_e%' 
  AND (artist_id = 1111111 OR artist_id = 2222222);

-- 4. Мин., средняя, макс., сумма и количество композиций
SELECT 
    MIN(duration) AS min_duration,
    AVG(duration) AS avg_duration,
    MAX(duration) AS max_duration,
    SUM(duration) AS total_duration,
    COUNT(*) AS total_tracks
FROM track;

-- 5. Названия альбомов исполнителя Bond
SELECT album.title
FROM album
JOIN artist ON album.artist_id = artist.artist_id
WHERE artist.name = 'Bond';

-- 6. Название альбома и самая короткая композиция (>= 5 минут)
SELECT a.title AS album_title, t.title AS track_title, t.duration
FROM album a
JOIN track t ON a.album_id = t.album_id
WHERE t.duration = (
    SELECT MIN(t2.duration)
    FROM track t2
    WHERE t2.album_id = a.album_id
)
AND EXTRACT(MINUTE FROM t.duration) >= 5;

-- 7. Композиции, длительность которых выше средней
SELECT title, duration
FROM track
WHERE duration > (SELECT AVG(duration) FROM track);

-- 8. Композиции, длительность которых больше любой композиции альбома 'Evolve'
-- Способ 1
SELECT t.title, t.duration
FROM track t
WHERE t.duration > ANY (
    SELECT t2.duration
    FROM track t2
    JOIN album a ON t2.album_id = a.album_id
    WHERE a.title = 'Evolve'
);

-- Способ 2
SELECT t.title, t.duration
FROM track t
WHERE EXISTS (
    SELECT 1
    FROM track t2
    JOIN album a ON t2.album_id = a.album_id
    WHERE a.title = 'Evolve'
      AND t.duration > t2.duration
);

-- 9. Композиции, длительность которых больше каждой композиции альбома 'Evolve'
-- Способ 1
SELECT t.title, t.duration
FROM track t
WHERE t.duration > ALL (
    SELECT t2.duration
    FROM track t2
    JOIN album a ON t2.album_id = a.album_id
    WHERE a.title = 'Evolve'
);

-- Способ 2
SELECT t.title, t.duration
FROM track t
WHERE t.duration > (
    SELECT MAX(t2.duration)
    FROM track t2
    JOIN album a ON t2.album_id = a.album_id
    WHERE a.title = 'Evolve'
);

-- 10. Композиции с такой же длительностью, как у композиции Girl
SELECT t.title
FROM track t
WHERE t.duration = (
    SELECT duration
    FROM track
    WHERE title = 'Girl'
);
