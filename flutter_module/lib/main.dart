import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:package_info/package_info.dart';

import 'helpScreen.dart';

Future<void> main() async {
  final PackageInfo packageInfo = await PackageInfo.fromPlatform();
  runApp(Directionality(
      textDirection: TextDirection.ltr, child: Router(packageInfo)));
}

class Router extends StatelessWidget {
  Router(this.packageInfo);

  final PackageInfo packageInfo;

  @override
  Widget build(BuildContext context) {
    final String route = window.defaultRouteName;
    switch (route) {
      case 'helpScreen':
        return Container(
          child: new HelpScreenWidget(),
          color: Colors.black,
        );

      default:
        return Container(
          child: Center(child: Text('Unknown route: $route')),
          color: Colors.red,
        );
    }
  }
}
