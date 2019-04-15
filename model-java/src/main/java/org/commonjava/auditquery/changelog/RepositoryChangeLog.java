/**
 * Copyright (C) 2013~2019 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.auditquery.changelog;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity
@Indexed
public class RepositoryChangeLog
	implements Serializable
{
	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private String storeKey;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private Date changeTime;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private String version;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private String summary;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private RepoChangeType changeType;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private String user;

	@JsonProperty
	@Field( index = Index.YES, analyze = Analyze.NO )
	private String diffContent;

	public String getStoreKey()
	{
		return storeKey;
	}

	public void setStoreKey( String storeKey )
	{
		this.storeKey = storeKey;
	}

	public Date getChangeTime()
	{
		return changeTime;
	}

	public void setChangeTime( Date changeTime )
	{
		this.changeTime = changeTime;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion( String version )
	{
		this.version = version;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary( String summary )
	{
		this.summary = summary;
	}

	public RepoChangeType getChangeType()
	{
		return changeType;
	}

	public void setChangeType( RepoChangeType changeType )
	{
		this.changeType = changeType;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser( String user )
	{
		this.user = user;
	}

	public String getDiffContent()
	{
		return diffContent;
	}

	public void setDiffContent( String diffContent )
	{
		this.diffContent = diffContent;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( storeKey == null ) ? 1 : storeKey.hashCode() );
		result = prime * result + ( ( changeTime == null ) ? 2 : changeTime.hashCode() );
		result = prime * result + ( ( version == null ) ? 3 : version.hashCode() );
		result = prime * result + ( ( summary == null ) ? 5 : summary.hashCode() );
		result = prime * result + ( ( changeType == null ) ? 8 : changeType.hashCode() );
		result = prime * result + ( ( user == null ) ? 13 : user.hashCode() );
		result = prime * result + ( ( diffContent == null ) ? 21 : diffContent.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( getClass() != obj.getClass() )
		{
			return false;
		}
		final RepositoryChangeLog other = (RepositoryChangeLog) obj;
		if ( storeKey == null )
		{
			if ( other.storeKey != null )
			{
				return false;
			}
		}
		else if ( !storeKey.equals( other.storeKey ) )
		{
			return false;
		}
		if ( changeTime == null )
		{
			if ( other.changeTime != null )
			{
				return false;
			}
		}
		else if ( !changeTime.equals( other.changeTime ) )
		{
			return false;
		}

		if ( version == null )
		{
			if ( other.version != null )
			{
				return false;
			}
		}
		else if ( !version.equals( other.version ) )
		{
			return false;
		}

		if ( summary == null )
		{
			if ( other.summary != null )
			{
				return false;
			}
		}
		else if ( !summary.equals( other.summary ) )
		{
			return false;
		}

		if ( changeType == null )
		{
			if ( other.changeType != null )
			{
				return false;
			}
		}
		else if ( !changeType.equals( other.changeType ) )
		{
			return false;
		}

		if ( user == null )
		{
			if ( other.user != null )
			{
				return false;
			}
		}
		else if ( !user.equals( other.user ) )
		{
			return false;
		}

		if ( diffContent == null )
		{
			if ( other.diffContent != null )
			{
				return false;
			}
		}
		else if ( !diffContent.equals( other.diffContent ) )
		{
			return false;
		}

		return true;
	}
}
