
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

export default function Navigation() {
  const navigate = useNavigate();
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const menuItems = [
    { path: '/', label: '홈', icon: 'ri-home-line' },
    { path: '/attendance', label: '출석체크', icon: 'ri-calendar-check-line' },
    { path: '/offering', label: '헌금', icon: 'ri-hand-heart-line' },
    { path: '/qt', label: '큐티', icon: 'ri-book-open-line' },
    { path: '/notice', label: '공지사항', icon: 'ri-notification-line' },
    { path: '/diary', label: '일지', icon: 'ri-file-text-line' },
    { path: '/gallery', label: '사진첩', icon: 'ri-image-line' },
    { path: '/game', label: '미니게임', icon: 'ri-gamepad-line' },
    { path: '/bulletin', label: '주보', icon: 'ri-newspaper-line' },
    { path: '/music', label: '찬양악보', icon: 'ri-music-line' },
    { path: '/monthly', label: '월간출석', icon: 'ri-calendar-2-line' }
  ];

  return (
    <nav className="bg-white shadow-lg border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center">
            <h1 className="text-xl font-bold text-blue-600" style={{ fontFamily: '"Pacifico", serif' }}>
              청소년부 행정
            </h1>
          </div>
          
          {/* Desktop Menu */}
          <div className="hidden md:flex space-x-1">
            {menuItems.map((item) => (
              <button
                key={item.path}
                onClick={() => navigate(item.path)}
                className={`px-3 py-2 rounded-md text-sm font-medium transition-colors cursor-pointer whitespace-nowrap ${
                  location.pathname === item.path
                    ? 'bg-blue-100 text-blue-700'
                    : 'text-gray-600 hover:text-blue-600 hover:bg-blue-50'
                }`}
              >
                <i className={`${item.icon} mr-1`}></i>
                {item.label}
              </button>
            ))}
          </div>

          {/* Mobile Menu Button */}
          <div className="md:hidden">
            <button
              onClick={() => setIsMenuOpen(!isMenuOpen)}
              className="text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <i className={`text-xl ${isMenuOpen ? 'ri-close-line' : 'ri-menu-line'}`}></i>
            </button>
          </div>
        </div>

        {/* Mobile Menu */}
        {isMenuOpen && (
          <div className="md:hidden border-t border-gray-200">
            <div className="px-2 pt-2 pb-3 space-y-1">
              {menuItems.map((item) => (
                <button
                  key={item.path}
                  onClick={() => {
                    navigate(item.path);
                    setIsMenuOpen(false);
                  }}
                  className={`block w-full text-left px-3 py-2 rounded-md text-base font-medium transition-colors cursor-pointer ${
                    location.pathname === item.path
                      ? 'bg-blue-100 text-blue-700'
                      : 'text-gray-600 hover:text-blue-600 hover:bg-blue-50'
                  }`}
                >
                  <i className={`${item.icon} mr-2`}></i>
                  {item.label}
                </button>
              ))}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
}
