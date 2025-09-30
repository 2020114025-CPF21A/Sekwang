
import Navigation from '../../components/feature/Navigation.tsx';
import Card from '../../components/base/Card.tsx';
import Button from '../../components/base/Button.tsx';
import { useNavigate } from 'react-router-dom';

export default function Home() {
  const navigate = useNavigate();

  const quickActions = [
    { title: '출석체크', icon: 'ri-calendar-check-line', path: '/attendance', color: 'bg-blue-500' },
    { title: '헌금기록', icon: 'ri-hand-heart-line', path: '/offering', color: 'bg-green-500' },
    { title: '큐티작성', icon: 'ri-book-open-line', path: '/qt', color: 'bg-purple-500' },
    { title: '미니게임', icon: 'ri-gamepad-line', path: '/game', color: 'bg-orange-500' }
  ];

  const recentActivities = [
    { type: '출석', message: '김민수님이 출석했습니다', time: '10분 전' },
    { type: '큐티', message: '이지은님이 큐티를 작성했습니다', time: '30분 전' },
    { type: '헌금', message: '박준호님이 헌금을 드렸습니다', time: '1시간 전' },
    { type: '공지', message: '새로운 공지사항이 등록되었습니다', time: '2시간 전' }
  ];

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />
      
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Hero Section */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-800 mb-4">청소년부 행정 시스템</h1>
          <p className="text-xl text-gray-600">출석, 헌금, 큐티를 체계적으로 관리하세요</p>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          {quickActions.map((action, index) => (
            <Card key={index} className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => navigate(action.path)}>
              <div className="text-center">
                <div className={`w-16 h-16 ${action.color} rounded-full flex items-center justify-center mx-auto mb-4`}>
                  <i className={`${action.icon} text-2xl text-white`}></i>
                </div>
                <h3 className="text-lg font-semibold text-gray-800 mb-2">{action.title}</h3>
                <Button onClick={() => navigate(action.path)} size="sm">
                  바로가기
                </Button>
              </div>
            </Card>
          ))}
        </div>

        {/* Dashboard Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Today's Stats */}
          <Card title="오늘의 현황">
            <div className="space-y-4">
              <div className="flex justify-between items-center">
                <span className="text-gray-600">출석자 수</span>
                <span className="text-2xl font-bold text-blue-600">24명</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">헌금 참여</span>
                <span className="text-2xl font-bold text-green-600">18명</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-gray-600">큐티 작성</span>
                <span className="text-2xl font-bold text-purple-600">12명</span>
              </div>
            </div>
          </Card>

          {/* Recent Activities */}
          <Card title="최근 활동">
            <div className="space-y-3">
              {recentActivities.map((activity, index) => (
                <div key={index} className="flex items-start space-x-3">
                  <div className="w-2 h-2 bg-blue-500 rounded-full mt-2"></div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-800">{activity.message}</p>
                    <p className="text-xs text-gray-500">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </Card>

          {/* Quick Links */}
          <Card title="바로가기">
            <div className="space-y-3">
              <Button onClick={() => navigate('/notice')} variant="secondary" className="w-full justify-start">
                <i className="ri-notification-line mr-2"></i>
                공지사항
              </Button>
              <Button onClick={() => navigate('/gallery')} variant="secondary" className="w-full justify-start">
                <i className="ri-image-line mr-2"></i>
                사진첩
              </Button>
              <Button onClick={() => navigate('/bulletin')} variant="secondary" className="w-full justify-start">
                <i className="ri-newspaper-line mr-2"></i>
                주보 보기
              </Button>
              <Button onClick={() => navigate('/monthly')} variant="secondary" className="w-full justify-start">
                <i className="ri-calendar-2-line mr-2"></i>
                월간 출석표
              </Button>
            </div>
          </Card>
        </div>

        {/* Weekly Progress */}
        <Card title="이번 주 진행상황" className="mt-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center">
              <div className="w-20 h-20 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-calendar-check-line text-2xl text-blue-600"></i>
              </div>
              <h4 className="font-semibold text-gray-800 mb-1">출석률</h4>
              <p className="text-2xl font-bold text-blue-600">85%</p>
            </div>
            <div className="text-center">
              <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-hand-heart-line text-2xl text-green-600"></i>
              </div>
              <h4 className="font-semibold text-gray-800 mb-1">헌금 참여율</h4>
              <p className="text-2xl font-bold text-green-600">72%</p>
            </div>
            <div className="text-center">
              <div className="w-20 h-20 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-3">
                <i className="ri-book-open-line text-2xl text-purple-600"></i>
              </div>
              <h4 className="font-semibold text-gray-800 mb-1">큐티 작성률</h4>
              <p className="text-2xl font-bold text-purple-600">68%</p>
            </div>
          </div>
        </Card>
      </div>
    </div>
  );
}
