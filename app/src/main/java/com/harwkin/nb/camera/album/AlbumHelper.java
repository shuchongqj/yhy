package com.harwkin.nb.camera.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.quanyan.yhy.R;
import com.yhy.common.beans.album.MediaItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * Select multiple pictures pictures selection mechanismalbum thumbnails album
 * picture album number each album of pictures
 * 
 * @author zhaocheng
 * 
 */
public class AlbumHelper {
    final String TAG = getClass().getSimpleName();
    final String ALL_KEY_PHOTO = "ALL_PHOTO";
    final String ALL_KEY_VIDEO = "ALL_VIDEO";
    Context context;
    ContentResolver cr;
    /**
     * The thumbnail list
     */
    HashMap<String, String> thumbnailList = new HashMap<>();

    /**
     * album list
     */
    List<HashMap<String, String>> albumList = new ArrayList<>();
    HashMap<String, MediaBucket> bucketList = new HashMap<>();

    private static AlbumHelper instance;

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    /**
     * init
     * 
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    /**
     * get ImageThumbnail
     */
    private void getImageThumbnail() {
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA};
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        getImageThumbnail(cursor);
    }

    /**
     * Thumbnail images from the database
     *
     * @param cur
     */
    private void getImageThumbnail(Cursor cur) {
        if(cur == null){
            return;
        }
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * Get the original image
     */
    void getAlbum() {
        String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART,
                Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
        Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
                null, null);
        getAlbumColumnData(cursor);

    }

    /**
     * The original image from the local database
     * 
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;

            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);

                // Do something with the values.
//                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt
//                        + "albumKey: " + albumKey + " artist: " + artist
//                        + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);

            } while (cur.moveToNext());

        }
    }

    /**
     * Whether to create a photo collections
     */
    boolean hasBuildMediaBucketList = false;

    /**
     * Get photo collections
     */
    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();

        getImageThumbnail();

        String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME,Media.DATE_TAKEN };
        // Get a cursor
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                Media.DATE_TAKEN+" DESC");
        MediaBucket allPhoto = new MediaBucket();
        allPhoto.count = cur.getCount();
        allPhoto.bucketName = context.getResources().getString(R.string.all_photo);
        allPhoto.mediaList = new ArrayList<>();
        if (cur.moveToFirst()) {
            // Obtain the specified column index
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur
                    .getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            int photoDateIndex = cur.getColumnIndex(Media.DATE_TAKEN);
            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);
                long time = cur.getLong(photoDateIndex);
//                Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
//                        + picasaId + " name:" + name + " path:" + path
//                        + " title: " + title + " size: " + size + " bucket: "
//                        + bucketName + "---"+"time---->"+time);

                MediaBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new MediaBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.mediaList = new ArrayList<>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                MediaItem mediaItem = new MediaItem();
                mediaItem.mediaId = _id;
                mediaItem.mediaPath = path;
                mediaItem.mediaCreateTime =time;
                mediaItem.thumbnailPath = thumbnailList.get(_id);
                bucket.mediaList.add(mediaItem);
                allPhoto.mediaList.add(mediaItem);
            } while (cur.moveToNext());
        }

        bucketList.put(ALL_KEY_PHOTO, allPhoto);
        Iterator<Entry<String, MediaBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, MediaBucket> entry = itr.next();
            MediaBucket bucket = entry.getValue();
            for (int i = 0; i < bucket.mediaList.size(); ++i) {
                MediaItem image = bucket.mediaList.get(i);
            }
        }
        hasBuildMediaBucketList = true;
        long endTime = System.currentTimeMillis();
    }

    /**
     * get VideoThumbnail
     */
    private void getVideoThumbnail() {
        String[] projection = { MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.VIDEO_ID, MediaStore.Video.Thumbnails.DATA};
        Cursor cursor = cr.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getVideoThumbnail(cursor);
    }

    /**
     * Thumbnail video from the database
     * @param cur
     */
    private void getVideoThumbnail(Cursor cur) {
        if(cur == null){
            return;
        }
        if (cur.moveToFirst()) {
            int video_id;
            String video_path;
            int video_idColumn = cur.getColumnIndex(MediaStore.Video.Thumbnails._ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
            do {
                video_id = cur.getInt(video_idColumn);
                video_path = cur.getString(dataColumn);
                thumbnailList.put("" + video_id, video_path);
            } while (cur.moveToNext());
        }
    }

    /********************************************************************         对查询 Media的解耦           ****************************************************************************/

    /**
     * 查询并获取所有Pic
     */
    public MediaBucket getAllPicBucket() {
        //初始化一个装Media的篮子
        MediaBucket bucket = new MediaBucket();
        bucket.count = 0;
        bucket.bucketName = context.getResources().getString(R.string.all_photo);
        bucket.mediaList = new ArrayList<>();
        //获取所有图片
        getImageThumbnail();
        String p_columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID,
                Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.DATE_TAKEN};
        // Get a cursor
        Cursor p_cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, p_columns, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (p_cur.moveToFirst()) {
            // Obtain the specified column index
            int photoIDIndex = p_cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = p_cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = p_cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = p_cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = p_cur.getColumnIndexOrThrow(Media.SIZE);
            int picasaIdIndex = p_cur.getColumnIndexOrThrow(Media.PICASA_ID);
            int photoDateIndex = p_cur.getColumnIndex(Media.DATE_TAKEN);
            do {
                String _id = p_cur.getString(photoIDIndex);
                String name = p_cur.getString(photoNameIndex);
                String path = p_cur.getString(photoPathIndex);
                String title = p_cur.getString(photoTitleIndex);
                long size = p_cur.getLong(photoSizeIndex);
                String picasaId = p_cur.getString(picasaIdIndex);
                long time = p_cur.getLong(photoDateIndex);

                bucket.count++;
                MediaItem mediaItem = new MediaItem();
                mediaItem.mediaId = _id;
                mediaItem.mediaPath = path;
                mediaItem.mediaCreateTime = time;
                mediaItem.thumbnailPath = thumbnailList.get(_id);
                mediaItem.size = size;
                bucket.mediaList.add(mediaItem);
            } while (p_cur.moveToNext());
        }
        p_cur.close();
        return bucket;
    }

    /**
     * 查询并获取所有Video
     */
    public MediaBucket getAllVideoBucket() {
        //初始化一个装Media的篮子
        MediaBucket bucket = new MediaBucket();
        bucket.count = 0;
        bucket.bucketName = context.getResources().getString(R.string.all_video);
        bucket.mediaList = new ArrayList<>();
        //获取所有视频
        getVideoThumbnail();
        String v_columns[] = new String[]{
                MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_TAKEN};
        // Get a cursor
        Cursor v_cur = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, v_columns, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (v_cur.moveToFirst()) {
            // Obtain the specified column index
            int videoIDIndex = v_cur.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int videoPathIndex = v_cur.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int videoNameIndex = v_cur.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int videoTitleIndex = v_cur.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int videoSizeIndex = v_cur.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int videoDurationIndex = v_cur.getColumnIndex(MediaStore.Video.Media.DURATION);
            int videoDateIndex = v_cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
            do {
                String _id = v_cur.getString(videoIDIndex);
                String name = v_cur.getString(videoNameIndex);
                String path = v_cur.getString(videoPathIndex);
                String title = v_cur.getString(videoTitleIndex);
                long size = v_cur.getLong(videoSizeIndex);
                long duration = v_cur.getLong(videoDurationIndex);
                long time = v_cur.getLong(videoDateIndex);
                bucket.count++;
                MediaItem mediaItem = new MediaItem();
                mediaItem.mediaId = _id;
                mediaItem.mediaPath = path;
                mediaItem.mediaCreateTime = time;
                mediaItem.thumbnailPath = thumbnailList.get(_id);
                mediaItem.size = size;
                mediaItem.duration = duration;
                mediaItem.mediaType = 2;
                String postfix = mediaItem.mediaPath.substring(mediaItem.mediaPath.lastIndexOf("."));
                if (!".TS".equalsIgnoreCase(postfix) && size < 1024 * 1024 * 300) bucket.mediaList.add(mediaItem);
            } while (v_cur.moveToNext());
        }
        v_cur.close();
        return bucket;
    }

    /**
     * 查询并获取获取所有pic和video
     */
    public MediaBucket getAllPic_ViBucket() {
        //初始化一个装Media的篮子
        MediaBucket bucket = new MediaBucket();
        bucket.count = 0;
        bucket.bucketName = context.getResources().getString(R.string.all_pic_vi);
        bucket.mediaList = new ArrayList<>();
        MediaBucket bucket_p = getAllPicBucket();
        MediaBucket bucket_v = getAllVideoBucket();
        bucket.count = bucket_p.count + bucket_v.count;
        bucket.mediaList.addAll(bucket_v.mediaList);
        bucket.mediaList.addAll(bucket_p.mediaList);
        return bucket;
    }

    /**
     * 获取所有图片
     * @param refresh
     * @return
     */
    public List<MediaBucket> getImageBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildMediaBucketList)) {
            bucketList.clear();
            buildImagesBucketList();
        }
        List<MediaBucket> tmpList = new ArrayList<>();
        Iterator<Entry<String, MediaBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, MediaBucket> entry = itr.next();
            if (ALL_KEY_PHOTO.equals(entry.getKey()))
                tmpList.add(0, entry.getValue());
            else
                tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 获取所有和视频
     * @param refresh
     * @return
     */
    public List<MediaBucket> getImageAndVideoBucketList(boolean refresh) {
        List<MediaBucket> tmpList = new ArrayList<>();
        tmpList.add(getAllPic_ViBucket());
        Iterator<Entry<String, MediaBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, MediaBucket> entry = itr.next();
            if (ALL_KEY_PHOTO.equals(entry.getKey()))
                tmpList.add(1, entry.getValue());
            else
                tmpList.add(entry.getValue());
        }
        return tmpList;
    }
/**
     * 获取视频
     * @param refresh
     * @return
     */
    public List<MediaBucket> getVideoBucketList(boolean refresh) {
        List<MediaBucket> tmpList = new ArrayList<>();
        tmpList.add(getAllVideoBucket());
        return tmpList;
    }

    String getOriginalImagePath(String image_id) {
        String path = null;
//        Log.i(TAG, "---(^o^)----" + image_id);
        String[] projection = { Media._ID, Media.DATA };
        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
                Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));

        }
        return path;
    }
}
