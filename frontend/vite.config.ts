// vite.config.ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import { resolve } from 'node:path'
import AutoImport from 'unplugin-auto-import/vite'

// dev 에서는 '/', build(배포) 에서는 '/app/'가 기본.
// 환경변수 BASE_PATH가 있으면 우선 적용 (예: BASE_PATH=/foo/)
const computeBase = (mode: string) => {
  const envBase = process.env.BASE_PATH
  if (envBase) return envBase.endsWith('/') ? envBase : `${envBase}/`
  return mode === 'development' ? '/' : '/app/'
}

export default defineConfig(({ mode }) => {
  const base = computeBase(mode)
  const isPreview = !!process.env.IS_PREVIEW

  return {
    define: {
      __BASE_PATH__: JSON.stringify(base),
      __IS_PREVIEW__: JSON.stringify(isPreview),
    },
    plugins: [
      react(),
      AutoImport({
        imports: [
          {
            react: [
              'React','useState','useEffect','useContext','useReducer','useCallback',
              'useMemo','useRef','useImperativeHandle','useLayoutEffect','useDebugValue',
              'useDeferredValue','useId','useInsertionEffect','useSyncExternalStore',
              'useTransition','startTransition','lazy','memo','forwardRef',
              'createContext','createElement','cloneElement','isValidElement'
            ],
          },
          {
            'react-router-dom': [
              'useNavigate','useLocation','useParams','useSearchParams',
              'Link','NavLink','Navigate','Outlet'
            ],
          },
          { 'react-i18next': ['useTranslation','Trans'] },
        ],
        dts: true,
      }),
    ],
    base,
    build: {
      outDir: 'dist',       // Spring으로 복사할 폴더 (postbuild 스크립트와 일치)
      sourcemap: true,
      assetsDir: 'assets',
    },
    resolve: {
      alias: { '@': resolve(__dirname, './src') },
    },
    server: {
      host: '0.0.0.0',
      port: 3000,
      // 백엔드(Spring Boot 8080)로 API 프록시
      proxy: {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
        },
      },
    },
  }
})
