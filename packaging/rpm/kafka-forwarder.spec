Name:     kafka-forwarder
Version:  %{__version}
Release:  %{__release}%{?dist}

License:  GNU AGPLv3
URL:  https://github.com/redBorder/kafka-forwarder
Source0: %{name}-%{version}.tar.gz

BuildRequires: maven java-devel

Summary: kafka-forwarder module
Requires: java

%description
%{summary}

%prep
%setup -qn %{name}-%{version}

%build
export MAVEN_OPTS="-Xmx512m -Xms256m -Xss10m -XX:MaxPermSize=512m" && mvn clean package

%install
mkdir -p %{buildroot}/usr/share/%{name}
install -D -m 644 target/%{name}-*-selfcontained.jar %{buildroot}/usr/share/%{name}/%{name}.jar

%clean
rm -rf %{buildroot}


%files
%defattr(755,root,root)
/usr/share/%{name}
%defattr(644,root,root)
/usr/share/%{name}/%{name}.jar

%changelog
* Tue Feb 27 2024 David Vanhoucke <dvanhoucke@redborder.com> - 0.0.1
- first spec version
