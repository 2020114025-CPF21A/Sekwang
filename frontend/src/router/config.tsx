
import type { RouteObject } from "react-router-dom";
import NotFound from "../pages/NotFound.tsx";
import Home from "../pages/home/page.tsx";
import Attendance from "../pages/attendance/page.tsx";
import Game from "../pages/game/page.tsx";
import Offering from "../pages/offering/page.tsx";
import QT from "../pages/qt/page.tsx";
import Notice from "../pages/notice/page.tsx";
import Gallery from "../pages/gallery/page.tsx";
import Diary from "../pages/diary/page.tsx";
import Bulletin from "../pages/bulletin/page.tsx";
import Music from "../pages/music/page.tsx";
import Monthly from "../pages/monthly/page.tsx";

const routes: RouteObject[] = [
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/attendance",
    element: <Attendance />,
  },
  {
    path: "/offering",
    element: <Offering />,
  },
  {
    path: "/qt",
    element: <QT />,
  },
  {
    path: "/notice",
    element: <Notice />,
  },
  {
    path: "/gallery",
    element: <Gallery />,
  },
  {
    path: "/diary",
    element: <Diary />,
  },
  {
    path: "/bulletin",
    element: <Bulletin />,
  },
  {
    path: "/music",
    element: <Music />,
  },
  {
    path: "/monthly",
    element: <Monthly />,
  },
  {
    path: "/game",
    element: <Game />,
  },
  {
    path: "*",
    element: <NotFound />,
  },
];

export default routes;
