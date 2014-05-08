package com.adrianavecchioli.findit.domain;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class RememberItem implements Parcelable {

	private String tag;
	private String imagePath;
	private Location location;
	private long addedDate;
	private String id;

	public RememberItem(String id,String tag, String imageURI, Location location,
			long addedDate) {
		this.id=id;
		this.tag = tag;
		this.imagePath = imageURI;
		this.location = location;
		this.addedDate = addedDate;
	}

	public RememberItem() {
		// TODO Auto-generated constructor stub
	}

	private RememberItem(Parcel source) {
		this.id=source.readString();
		this.tag = source.readString();
		this.imagePath = source.readString();
		this.addedDate = source.readLong();
		this.location = source.readParcelable(Location.class.getClassLoader());

	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setAddedDate(long addedDate) {
		this.addedDate = addedDate;
	}

	public long getAddedDate() {
		return addedDate;
	}

	@Override
	public String toString() {
		return "Item [tag=" + tag + ", image=" + imagePath + ", loc="
				+ location + ", added=" + addedDate + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(tag);
		dest.writeString(imagePath);
		dest.writeLong(addedDate);
		dest.writeParcelable(location, flags);
	}
	public static final Parcelable.Creator<RememberItem> CREATOR = new Parcelable.Creator<RememberItem>() {
		public RememberItem createFromParcel(Parcel in) {
			return new RememberItem(in);
		}

		public RememberItem[] newArray(int size) {
			return new RememberItem[size];
		}
	};

	public String getId() {
		return id;
	}
}
